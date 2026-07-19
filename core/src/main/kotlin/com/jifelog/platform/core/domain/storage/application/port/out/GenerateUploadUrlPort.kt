package com.jifelog.platform.core.domain.storage.application.port.out

import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult

interface GenerateUploadUrlPort {
    fun generateUploadUrl(objectKey: String, contentType: String): UploadUrlResult
}