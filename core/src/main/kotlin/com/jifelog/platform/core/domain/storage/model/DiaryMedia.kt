package com.jifelog.platform.core.domain.storage.model

import com.fasterxml.uuid.Generators
import java.time.Instant
import java.util.UUID

/**
 * diary 미디어(파일) 메타데이터 도메인 객체.
 *
 * 라이프사이클:
 * - [withoutId]       : presigned URL 발급 직후, diary 가 아직 없을 때
 * - [committed]     : diary 가 생성되며 실제 업로드가 확정된 상태 (etag 포함 가능)
 *
 * invariant: COMMITTED 일 때만 diaryId 가 존재해야 한다.
 *            (DB 의 chk_diary_when_committed 제약과 동일한 도메인 규칙)
 */
class DiaryMedia(
    val id: UUID,
    val diaryId: UUID?,
    val userInfoId: UUID,
    val bucketName: String,
    val objectKey: String,
    val originalName: String,
    val mimeType: String?,
    val fileSize: Long?,
    val etag: String?,
    val sortOrder: Int,
    val status: DiaryMediaStatus,
    val createdAt: Instant,
    val deletedAt: Instant?,
) {
    init {
        val hasDiary = diaryId != null
        val isCommitted = status == DiaryMediaStatus.COMMITTED
        require(isCommitted == hasDiary) {
            "DiaryMedia invariant violated: COMMITTED requires diaryId, PENDING forbids diaryId"
        }
    }

    companion object {
        fun withoutId(
            userInfoId: UUID,
            bucketName: String,
            objectKey: String,
            originalName: String,
        ): DiaryMedia = DiaryMedia(
            id = Generators.timeBasedEpochGenerator().generate(),
            diaryId = null,
            userInfoId = userInfoId,
            bucketName = bucketName,
            objectKey = objectKey,
            originalName = originalName,
            mimeType = null,
            fileSize = null,
            etag = null,
            sortOrder = 0,
            status = DiaryMediaStatus.PENDING,
            createdAt = Instant.now(),
            deletedAt = null,
        )

        fun withId(
            id: UUID,
            diaryId: UUID?,
            userInfoId: UUID,
            bucketName: String,
            objectKey: String,
            originalName: String,
            mimeType: String?,
            fileSize: Long?,
            etag: String?,
            sortOrder: Int,
            status: DiaryMediaStatus,
            createdAt: Instant,
            deletedAt: Instant?,
        ): DiaryMedia = DiaryMedia(
            id = id,
            diaryId = diaryId,
            userInfoId = userInfoId,
            bucketName = bucketName,
            objectKey = objectKey,
            originalName = originalName,
            mimeType = mimeType,
            fileSize = fileSize,
            etag = etag,
            sortOrder = sortOrder,
            status = status,
            createdAt = createdAt,
            deletedAt = deletedAt,
        )
    }
}
