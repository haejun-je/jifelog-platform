package com.jifelog.platform.core.domain.diary.application.port.`in`

import java.time.LocalDate
import java.util.UUID

data class GenerateUploadUrlCommand(
    val userInfoId: UUID,
    val fileName: String,
    val contentType: String,
    val entryDate: LocalDate,
)