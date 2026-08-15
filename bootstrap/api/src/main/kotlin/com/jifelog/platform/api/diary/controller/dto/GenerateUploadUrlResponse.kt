package com.jifelog.platform.api.diary.controller.dto

import java.time.Instant

data class GenerateUploadUrlResponse(
    val uploadUrl: String,
    val expiresAt: Instant,
    val formData: Map<String, String>,
    val maxFileSizeBytes: Long,
)