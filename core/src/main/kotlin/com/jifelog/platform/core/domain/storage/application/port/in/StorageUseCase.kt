package com.jifelog.platform.core.domain.storage.application.port.`in`

import java.util.UUID

interface StorageUseCase {
    fun generateUploadUrl(command: GenerateUploadUrlCommand): UploadUrlResult
}

data class GenerateUploadUrlCommand(
    val userInfoId: UUID,
    val objectKey: String,
    val originalName: String,
    val contentType: String,
)

data class UploadUrlResult(
    val uploadUrl: String,
    val expiresAt: java.time.Instant,
    val formData: Map<String, String>,
    val maxFileSizeBytes: Long,
)
