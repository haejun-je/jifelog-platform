package com.jifelog.platform.core.domain.storage.infrastructure.adapter

import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateUploadUrlPort
import com.jifelog.platform.core.config.minio.properties.MinioProperties
import io.minio.MinioClient
import io.minio.PostPolicy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

@Component
class MinioStorageAdapter(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties,
) : GenerateUploadUrlPort {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun generateUploadUrl(objectKey: String, contentType: String): UploadUrlResult {
        val expiryMinutes = minioProperties.presignedUrlExpiryMinutes
        val maxFileSizeBytes = minioProperties.maxFileSizeBytes
        val now = ZonedDateTime.now(ZoneOffset.UTC)

        val policy = PostPolicy(minioProperties.bucket, now.plusMinutes(expiryMinutes))
            .apply {
                addEqualsCondition("key", objectKey)
                addEqualsCondition("Content-Type", contentType)
                addContentLengthRangeCondition(0, maxFileSizeBytes)
            }

        val signedFormData = minioClient.getPresignedPostFormData(policy)

        val uploadUrl = "${minioProperties.endpoint.trimEnd('/')}/${minioProperties.bucket}"

        // S3/MinIO enforces the user-defined conditions (`key`, `Content-Type`) on the multipart
        // upload, but does not include them in the signed form-data returned by the SDK. Merge
        // them so the client only needs to attach the file part.
        val formData: Map<String, String> = signedFormData +
            mapOf(
                "key" to objectKey,
                "Content-Type" to contentType,
            )

        val expiresAt = Instant.now().plusSeconds(TimeUnit.MINUTES.toSeconds(expiryMinutes))

        return UploadUrlResult(
            uploadUrl = uploadUrl,
            expiresAt = expiresAt,
            formData = formData,
            maxFileSizeBytes = maxFileSizeBytes,
        )
    }
}
