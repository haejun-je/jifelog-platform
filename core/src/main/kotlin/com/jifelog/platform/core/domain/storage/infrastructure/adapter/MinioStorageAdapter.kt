package com.jifelog.platform.core.domain.storage.infrastructure.adapter

import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateUploadUrlPort
import com.jifelog.platform.core.config.minio.properties.MinioProperties
import io.minio.GetPresignedObjectUrlArgs
import io.minio.Http
import io.minio.MinioClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class MinioStorageAdapter(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties,
) : GenerateUploadUrlPort {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun generateUploadUrl(objectKey: String, contentType: String): UploadUrlResult {
        val expiryMinutes = minioProperties.presignedUrlExpiryMinutes

        val uploadUrl = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Http.Method.PUT)
                .bucket(minioProperties.bucket)
                .`object`(objectKey)
                .expiry(expiryMinutes.toInt(), TimeUnit.MINUTES)
                .extraQueryParams(mapOf("Content-Type" to contentType))
                .build()
        )

        val expiresAt = Instant.now().plusSeconds(expiryMinutes * 60)

        return UploadUrlResult(
            uploadUrl = uploadUrl,
            objectKey = objectKey,
            expiresAt = expiresAt,
        )
    }
}