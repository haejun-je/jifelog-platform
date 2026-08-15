package com.jifelog.platform.api.diary.controller

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryPhotoUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.GenerateUploadUrlCommand
import com.jifelog.platform.api.diary.controller.dto.GenerateUploadUrlRequest
import com.jifelog.platform.api.diary.controller.dto.GenerateUploadUrlResponse
import com.jifelog.security.jwt.api.JifelogUser
import com.jifelog.security.jwt.api.JifelogUserData
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/diaries/photos")
class DiaryPhotoController(
    private val diaryPhotoUseCase: DiaryPhotoUseCase,
) {

    @PostMapping("/upload-urls", version = "1")
    fun generateUploadUrl(
        @JifelogUser jifelogUser: JifelogUserData,
        @Valid @RequestBody request: GenerateUploadUrlRequest,
    ): ResponseEntity<GenerateUploadUrlResponse> {
        val command = GenerateUploadUrlCommand(
            userInfoId = UUID.fromString(jifelogUser.userId),
            fileName = request.fileName,
            contentType = request.contentType,
            entryDate = request.entryDate,
        )

        val result = diaryPhotoUseCase.generateUploadUrl(command)

        return ResponseEntity.ok(
            GenerateUploadUrlResponse(
                uploadUrl = result.uploadUrl,
                expiresAt = result.expiresAt,
                formData = result.formData,
                maxFileSizeBytes = result.maxFileSizeBytes,
            )
        )
    }
}