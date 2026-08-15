package com.jifelog.platform.core.domain.storage.application.port.out

/**
 * MinIO/S3 객체에 대한 presigned GET URL 발급 포트.
 *
 * - 반환되는 URL 은 AWS Signature V4 쿼리스트링이 포함된 일회성 서명이다.
 * - 만료 시간은 구현체에서 `MinioProperties.presignedUrlExpiryMinutes` 로 결정한다.
 */
interface GenerateDownloadUrlPort {
    fun presignGetObject(bucketName: String, objectKey: String): String
}