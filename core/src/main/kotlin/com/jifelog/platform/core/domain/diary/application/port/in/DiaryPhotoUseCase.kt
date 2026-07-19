package com.jifelog.platform.core.domain.diary.application.port.`in`

import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult

interface DiaryPhotoUseCase {
    fun generateUploadUrl(command: GenerateUploadUrlCommand): UploadUrlResult
}