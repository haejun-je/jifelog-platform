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
import com.jifelog.platform.core.domain.storage.application.port.out.ObjectStat
import com.jifelog.platform.core.domain.storage.application.port.out.SaveDiaryMediaPort
import com.jifelog.platform.core.domain.storage.application.port.out.SoftDeleteDiaryMediaPort
import com.jifelog.platform.core.domain.storage.application.port.out.StatObjectPort
import com.jifelog.platform.core.domain.storage.model.DiaryMedia
import com.jifelog.platform.core.domain.storage.model.FileMetadata
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.util.UUID

@Service
class StorageService(
    private val generateUploadUrlPort: GenerateUploadUrlPort,
    private val saveDiaryMediaPort: SaveDiaryMediaPort,
    private val loadDiaryMediaPort: LoadDiaryMediaPort,
    private val softDeleteDiaryMediaPort: SoftDeleteDiaryMediaPort,
    private val statObjectPort: StatObjectPort,
    private val minioProperties: MinioProperties,
    private val transactionTemplate: TransactionTemplate,
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

        // 1) PENDING row 를 한 번에 조회 (커밋 대상이 사용자의 것인지 필터) — DB read
        val diaryMediaByKey: Map<String, DiaryMedia> = try {
            loadDiaryMediaPort
                .findPendingByUserInfoIdAndObjectKeys(command.userInfoId, command.objectKeys)
                .associateBy { it.objectKey }
        } catch (e: Exception) {
            throw BusinessException(ErrorCode.ES_03_001, cause = e)
        }

        // 2) MinIO statObject 검증 — HTTP 호출이므로 트랜잭션 밖에서 수행한다.
        //    모든 key 의 검증을 마친 뒤 3) 에서 일괄 commit 하므로,
        //    중간 key 가 실패해도 앞선 key 가 부분 commit 되지 않는다.
        val prepared: List<Pair<DiaryMedia, ObjectStat>> = command.objectKeys.map { objectKey ->
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
            diaryMedia to stat
        }

        // 3) 검증을 통과한 media 를 한 트랜잭션으로 일괄 COMMITTED 저장
        try {
            transactionTemplate.execute {
                prepared.forEachIndexed { index, (diaryMedia, stat) ->
                    saveDiaryMediaPort.save(
                        diaryMedia.commit(
                            diaryId = command.diaryId,
                            metadata = FileMetadata(
                                etag = stat.etag,
                                size = stat.size,
                                mimeType = stat.contentType,
                            ),
                            sortOrder = index,
                        )
                    )
                }
            }
        } catch (e: Exception) {
            throw BusinessException(ErrorCode.ES_03_001, cause = e)
        }
    }

    /**
     * 일기 soft delete 시점에 같은 트랜잭션 안에서 호출된다.
     * 별도 트랜잭션을 만들지 않으므로 일기/미디어 soft delete 가 함께 commit/rollback 된다.
     */
    override fun softDeleteMediaForDiary(diaryId: UUID) {
        softDeleteDiaryMediaPort.softDeleteByDiaryId(diaryId)
    }
}
