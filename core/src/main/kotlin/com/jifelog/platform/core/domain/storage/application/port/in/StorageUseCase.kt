package com.jifelog.platform.core.domain.storage.application.port.`in`

import java.util.UUID

interface StorageUseCase {
    fun generateUploadUrl(command: GenerateUploadUrlCommand): UploadUrlResult

    /**
     * objectKey 목록에 해당하는 PENDING 미디어 메타데이터를 MinIO statObject 로 검증하고
     * COMMITTED 상태로 승격한다 (fk_diary_id, etag, file_size, mime_type, sort_order 갱신).
     */
    fun commitMediaForDiary(command: CommitDiaryMediaCommand)
}

data class GenerateUploadUrlCommand(
    val userInfoId: UUID,
    val objectKey: String,
    val originalName: String,
    val contentType: String,
)

data class CommitDiaryMediaCommand(
    val userInfoId: UUID,
    val diaryId: UUID,
    val objectKeys: List<String>,
)

data class UploadUrlResult(
    val uploadUrl: String,
    val expiresAt: java.time.Instant,
    val formData: Map<String, String>,
    val maxFileSizeBytes: Long,
)
