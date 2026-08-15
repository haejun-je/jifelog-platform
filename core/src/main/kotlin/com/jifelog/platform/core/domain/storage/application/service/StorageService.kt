package com.jifelog.platform.core.domain.storage.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.config.minio.properties.MinioProperties
import com.jifelog.platform.core.domain.storage.application.port.`in`.CommitDiaryMediaCommand
import com.jifelog.platform.core.domain.storage.application.port.`in`.GenerateUploadUrlCommand
import com.jifelog.platform.core.domain.storage.application.port.`in`.StorageUseCase
import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateUploadUrlPort
import com.jifelog.platform.core.domain.storage.application.port.out.LoadDiaryMediaPort
import com.jifelog.platform.core.domain.storage.application.port.out.SaveDiaryMediaPort
import com.jifelog.platform.core.domain.storage.application.port.out.StatObjectPort
import com.jifelog.platform.core.domain.storage.model.DiaryMedia
import com.jifelog.platform.core.domain.storage.model.FileMetadata
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StorageService(
    private val generateUploadUrlPort: GenerateUploadUrlPort,
    private val saveDiaryMediaPort: SaveDiaryMediaPort,
    private val loadDiaryMediaPort: LoadDiaryMediaPort,
    private val statObjectPort: StatObjectPort,
    private val minioProperties: MinioProperties,
) : StorageUseCase {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun generateUploadUrl(command: GenerateUploadUrlCommand): UploadUrlResult {
        return try {
            // 1) PENDING 메타데이터를 먼저 등록한다.
            //    URL 발급에 실패하더라도 DB 에 PENDING row 가 남아 cleanup 스케줄러가 정리한다.
            saveDiaryMediaPort.save(
                DiaryMedia.withoutId(
                    userInfoId = command.userInfoId,
                    bucketName = minioProperties.bucket,
                    objectKey = command.objectKey,
                    originalName = command.originalName,
                )
            )

            // 2) presigned URL 발급
            generateUploadUrlPort.generateUploadUrl(command)
        } catch (e: Exception) {
            throw BusinessException(ErrorCode.ES_03_001, cause = e)
        }
    }

    override fun commitMediaForDiary(command: CommitDiaryMediaCommand) {
        if (command.objectKeys.isEmpty()) return

        // 1) PENDING row 를 한 번에 조회 (커밋 대상이 사용자의 것인지 필터)
        val diaryMediaByKey: Map<String, DiaryMedia> = try {
            loadDiaryMediaPort
                .findPendingByUserInfoIdAndObjectKeys(command.userInfoId, command.objectKeys)
                .associateBy { it.objectKey }
        } catch (e: Exception) {
            throw BusinessException(ErrorCode.ES_03_001, cause = e)
        }

        // 2) 입력 순서대로 처리하면서 sort_order 를 인덱스로 결정
        command.objectKeys.forEachIndexed { index, objectKey ->
            val diaryMedia = diaryMediaByKey[objectKey]
                ?: run {
                    log.warn(
                        "commitMediaForDiary: missing PENDING row for user={}, objectKey={}",
                        command.userInfoId, objectKey,
                    )
                    throw BusinessException(ErrorCode.EB_03_001)
                }

            val stat = try {
                statObjectPort.statObject(diaryMedia.bucketName, objectKey)
            } catch (e: Exception) {
                // MinIO 측 응답 실패(미업로드/만료/권한 등)도 동일한 의미로 본다
                log.warn(
                    "commitMediaForDiary: statObject failed for bucket={}, objectKey={}",
                    diaryMedia.bucketName, objectKey, e,
                )
                throw BusinessException(ErrorCode.EB_03_001)
            }

            val committedDiaryMedia = diaryMedia.commit(
                diaryId = command.diaryId,
                metadata = FileMetadata(
                    etag = stat.etag,
                    size = stat.size,
                    mimeType = stat.contentType,
                ),
                sortOrder = index,
            )

            try {
                saveDiaryMediaPort.save(committedDiaryMedia)
            } catch (e: Exception) {
                throw BusinessException(ErrorCode.ES_03_001, cause = e)
            }
        }
    }
}
