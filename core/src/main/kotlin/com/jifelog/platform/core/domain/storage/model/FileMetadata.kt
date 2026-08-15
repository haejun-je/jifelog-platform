package com.jifelog.platform.core.domain.storage.model

data class FileMetadata(
    val etag: String,
    val size: Long,
    val mimeType: String,
)
