package com.jifelog.platform.web.diary.controller.dto

import java.time.Instant

data class GenerateUploadUrlResponse(
    val uploadUrl: String,
    val objectKey: String,
    val expiresAt: Instant,
)