package com.jifelog.platform.core.domain.storage.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.config.minio.properties.MinioProperties
import com.jifelog.platform.core.domain.storage.application.port.`in`.GenerateUploadUrlCommand
import com.jifelog.platform.core.domain.storage.application.port.`in`.StorageUseCase
import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateUploadUrlPort
import com.jifelog.platform.core.domain.storage.application.port.out.SaveDiaryMediaPort
import com.jifelog.platform.core.domain.storage.model.DiaryMedia
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StorageService(
    private val generateUploadUrlPort: GenerateUploadUrlPort,
    private val saveDiaryMediaPort: SaveDiaryMediaPort,
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
        } catch (e: BusinessException) {
            throw e
        } catch (e: Exception) {
            throw BusinessException(ErrorCode.ES_03_001, cause = e)
        }
    }
}
