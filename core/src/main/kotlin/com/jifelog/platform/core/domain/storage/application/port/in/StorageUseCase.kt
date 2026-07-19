package com.jifelog.platform.core.domain.storage.application.port.`in`

interface StorageUseCase {
    fun generateUploadUrl(objectKey: String, contentType: String): UploadUrlResult
}

data class UploadUrlResult(
    val uploadUrl: String,
    val objectKey: String,
    val expiresAt: java.time.Instant,
)