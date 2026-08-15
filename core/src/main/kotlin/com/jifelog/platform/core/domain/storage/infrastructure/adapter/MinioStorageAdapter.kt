package com.jifelog.platform.core.domain.storage.infrastructure.adapter

import com.jifelog.platform.core.domain.storage.application.port.`in`.GenerateUploadUrlCommand
import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateDownloadUrlPort
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateUploadUrlPort
import com.jifelog.platform.core.config.minio.properties.MinioProperties
import io.minio.GetPresignedObjectUrlArgs
import io.minio.Http
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
) : GenerateUploadUrlPort, GenerateDownloadUrlPort {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun generateUploadUrl(command: GenerateUploadUrlCommand): UploadUrlResult {
        val expiryMinutes = minioProperties.presignedUrlExpiryMinutes
        val maxFileSizeBytes = minioProperties.maxFileSizeBytes
        val now = ZonedDateTime.now(ZoneOffset.UTC)

        val policy = PostPolicy(minioProperties.bucket, now.plusMinutes(expiryMinutes))
            .apply {
                addEqualsCondition("key", command.objectKey)
                addEqualsCondition("Content-Type", command.contentType)
                addContentLengthRangeCondition(0, maxFileSizeBytes)
            }

        val signedFormData = minioClient.getPresignedPostFormData(policy)

        val uploadUrl = "${minioProperties.endpoint.trimEnd('/')}/${minioProperties.bucket}"

        // S3/MinIO enforces the user-defined conditions (`key`, `Content-Type`) on the multipart
        // upload, but does not include them in the signed form-data returned by the SDK. Merge
        // them so the client only needs to attach the file part.
        val formData: Map<String, String> = signedFormData +
            mapOf(
                "key" to command.objectKey,
                "Content-Type" to command.contentType,
            )

        val expiresAt = Instant.now().plusSeconds(TimeUnit.MINUTES.toSeconds(expiryMinutes))

        return UploadUrlResult(
            uploadUrl = uploadUrl,
            expiresAt = expiresAt,
            formData = formData,
            maxFileSizeBytes = maxFileSizeBytes,
        )
    }

    override fun presignGetObject(bucketName: String, objectKey: String): String {
        val expirySeconds = TimeUnit.MINUTES.toSeconds(minioProperties.presignedUrlExpiryMinutes)
        return try {
            val args = GetPresignedObjectUrlArgs.builder()
                .method(Http.Method.GET)
                .bucket(bucketName)
                .`object`(objectKey)
                .expiry(expirySeconds.toInt())
                .build()
            minioClient.getPresignedObjectUrl(args)
        } catch (e: Exception) {
            // presign 자체는 네트워크 호출이 없지만, SDK 가 내부에서 IllegalArgumentException 등을 던질 수 있다.
            log.error("Failed to presign GET url. bucket={}, object={}", bucketName, objectKey, e)
            throw e
        }
    }
}
