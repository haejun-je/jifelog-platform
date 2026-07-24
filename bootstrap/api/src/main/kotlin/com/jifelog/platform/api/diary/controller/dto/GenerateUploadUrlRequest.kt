package com.jifelog.platform.api.diary.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class GenerateUploadUrlRequest(
    @field:NotBlank
    val fileName: String,

    @field:NotBlank
    val contentType: String,

    @field:NotNull
    val entryDate: LocalDate,
)