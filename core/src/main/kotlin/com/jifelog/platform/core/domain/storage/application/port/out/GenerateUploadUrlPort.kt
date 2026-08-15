package com.jifelog.platform.core.domain.storage.application.port.out

import com.jifelog.platform.core.domain.storage.application.port.`in`.GenerateUploadUrlCommand
import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult

interface GenerateUploadUrlPort {
    fun generateUploadUrl(command: GenerateUploadUrlCommand): UploadUrlResult
}
