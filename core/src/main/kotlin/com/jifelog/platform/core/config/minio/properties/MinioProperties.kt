package com.jifelog.platform.core.config.minio.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "minio")
data class MinioProperties(
    val endpoint: String,
    val accessKey: String,
    val secretKey: String,
    val bucket: String,
    val presignedUrlExpiryMinutes: Long = 15,
    val maxFileSizeBytes: Long = 5L * 1024 * 1024,
)