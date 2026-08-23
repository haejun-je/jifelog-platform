package com.jifelog.platform.core.domain.storage.infrastructure.repository

import com.jifelog.platform.core.domain.storage.infrastructure.entity.DiaryMediaEntity
import com.jifelog.platform.core.domain.storage.model.DiaryMediaStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

interface DiaryMediaJpaRepository : JpaRepository<DiaryMediaEntity, UUID> {
    fun findAllByUserInfoIdAndObjectKeyInAndStatusAndDeletedAtIsNull(
        userInfoId: UUID,
        objectKeys: List<String>,
        status: DiaryMediaStatus,
    ): List<DiaryMediaEntity>

    /**
     * 여러 diary 의 미리보기 미디어(sort_order = 0, status 일치) 를 일괄 조회한다.
     *
     * - sort_order = 0 조건으로 diary 1건당 최대 1행만 반환된다 (목록 미리보기 정책).
     * - 결과 정렬은 사용처에 영향을 주지 않으므로 두지 않는다.
     */
    @Query(
        """
        SELECT m FROM DiaryMediaEntity m
        WHERE m.userInfoId = :userInfoId
          AND m.diaryId IN :diaryIds
          AND m.status = :status
          AND m.sortOrder = 0
          AND m.deletedAt IS NULL
        """,
    )
    fun findPreviewMediaByUserInfoIdAndDiaryIdsInAndStatus(
        @Param("userInfoId") userInfoId: UUID,
        @Param("diaryIds") diaryIds: List<UUID>,
        @Param("status") status: DiaryMediaStatus,
    ): List<DiaryMediaEntity>

    /**
     * 여러 diary 의 COMMITTED 활성 미디어 전체를 `sort_order` 오름차순으로 일괄 조회한다.
     *
     * - 단건 조회 응답의 다건 presigned URL 발급에 사용된다.
     * - 정렬은 `diary_id ASC, sort_order ASC` 이며, 같은 일기 내에서는 sort_order 순서대로 반환된다.
     * - 사용처에서 diaryId 별로 groupBy 하여 `Map<UUID, List<DiaryMedia>>` 형태로 가공한다.
     */
    @Query(
        """
        SELECT m FROM DiaryMediaEntity m
        WHERE m.userInfoId = :userInfoId
          AND m.diaryId IN :diaryIds
          AND m.status = com.jifelog.platform.core.domain.storage.model.DiaryMediaStatus.COMMITTED
          AND m.deletedAt IS NULL
        ORDER BY m.diaryId ASC, m.sortOrder ASC
        """,
    )
    fun findAllCommittedMediaByUserInfoIdAndDiaryIdsIn(
        @Param("userInfoId") userInfoId: UUID,
        @Param("diaryIds") diaryIds: List<UUID>,
    ): List<DiaryMediaEntity>

    /**
     * 특정 diary 의 활성(deleted_at IS NULL) 미디어 전체를 soft delete 처리한다.
     * 일기 soft delete 시 같은 트랜잭션에서 호출된다.
     */
    @Modifying
    @Query(
        """
        UPDATE DiaryMediaEntity m
        SET m.deletedAt = :now
        WHERE m.diaryId = :diaryId
          AND m.deletedAt IS NULL
        """,
    )
    fun softDeleteByDiaryId(
        @Param("diaryId") diaryId: UUID,
        @Param("now") now: Instant,
    ): Int
}
