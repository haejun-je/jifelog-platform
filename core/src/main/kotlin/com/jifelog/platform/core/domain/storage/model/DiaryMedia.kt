package com.jifelog.platform.core.domain.storage.model

import com.fasterxml.uuid.Generators
import java.time.Instant
import java.util.UUID

/**
 * diary 미디어(파일) 도메인 객체 (Aggregate Root).
 *
 * 라이프사이클:
 * - [withoutId] : presigned URL 발급 직후, diary 가 아직 없을 때 (status = PENDING, metadata = null)
 * - [commit]    : diary 가 생성되며 실제 업로드가 확정된 상태 (status = COMMITTED, metadata set)
 *
 * invariant: COMMITTED 일 때만 diaryId 가 존재해야 한다.
 *            (DB 의 chk_diary_when_committed 제약과 동일한 도메인 규칙)
 *
 * - 파일에 대한 알려진 정보(etag / size / mime type) 는 [FileMetadata] 값 객체로 묶어 관리한다.
 * - PENDING 시점에는 stat 결과가 없으므로 metadata 는 null 이다.
 * - COMMITTED 후에는 stat 응답을 [FileMetadata] 로 만들어 aggregate 의 일부로 저장한다.
 */
class DiaryMedia(
    val id: UUID,
    val diaryId: UUID?,
    val userInfoId: UUID,
    val bucketName: String,
    val objectKey: String,
    val originalName: String,
    val metadata: FileMetadata?,
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

    /**
     * PENDING → COMMITTED 상태 전환.
     *
     * - 현재 상태가 [DiaryMediaStatus.PENDING] 일 때만 호출 가능 (이미 commit 된 row 를 다시 commit 하는 것은 불가)
     * - 결과 인스턴스의 init 불변식(COMMITTED ↔ diaryId) 이 강제된다
     */
    fun commit(
        diaryId: UUID,
        metadata: FileMetadata,
        sortOrder: Int,
    ): DiaryMedia {
        require(status == DiaryMediaStatus.PENDING) {
            "DiaryMedia.commit() requires PENDING; current=$status"
        }
        return DiaryMedia(
            id = id,
            diaryId = diaryId,
            userInfoId = userInfoId,
            bucketName = bucketName,
            objectKey = objectKey,
            originalName = originalName,
            metadata = metadata,
            sortOrder = sortOrder,
            status = DiaryMediaStatus.COMMITTED,
            createdAt = createdAt,
            deletedAt = deletedAt,
        )
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
            metadata = null,
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
            metadata: FileMetadata?,
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
            metadata = metadata,
            sortOrder = sortOrder,
            status = status,
            createdAt = createdAt,
            deletedAt = deletedAt,
        )
    }
}
