package com.jifelog.platform.core.domain.diary.infrastructure.repository

import com.jifelog.platform.core.domain.diary.infrastructure.entity.DiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

interface DiaryJpaRepository : JpaRepository<DiaryEntity, UUID> {
    // 활성 일기(deleted_at IS NULL)만 대상으로 한다.
    // soft delete 후에도 같은 (user_info_id, entry_date) 로 새 일기 생성을 허용하기 위해
    // 유니크 제약은 두지 않고, 조회에서만 활성 row 를 본다.
    fun existsByUserInfoIdAndEntryDateAndDeletedAtIsNull(userInfoId: UUID, entryDate: LocalDate): Boolean

    fun findByIdAndUserInfoIdAndDeletedAtIsNull(id: UUID, userInfoId: UUID): DiaryEntity?

    fun findAllByUserInfoIdAndDeletedAtIsNullOrderByEntryDateDesc(userInfoId: UUID): List<DiaryEntity>

    /**
     * 일기 soft delete. deleted_at IS NULL 일 때만 deletedAt 을 갱신해 멱등성을 보장한다.
     */
    @Modifying
    @Query(
        """
        UPDATE DiaryEntity d
        SET d.deletedAt = :now
        WHERE d.id = :id
          AND d.userInfoId = :userInfoId
          AND d.deletedAt IS NULL
        """,
    )
    fun softDeleteByIdAndUserInfoId(
        @Param("id") id: UUID,
        @Param("userInfoId") userInfoId: UUID,
        @Param("now") now: Instant,
    ): Int
}