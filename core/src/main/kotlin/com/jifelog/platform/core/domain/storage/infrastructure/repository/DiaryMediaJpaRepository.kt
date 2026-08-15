package com.jifelog.platform.core.domain.storage.infrastructure.repository

import com.jifelog.platform.core.domain.storage.infrastructure.entity.DiaryMediaEntity
import com.jifelog.platform.core.domain.storage.model.DiaryMediaStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface DiaryMediaJpaRepository : JpaRepository<DiaryMediaEntity, UUID> {
    fun findAllByUserInfoIdAndObjectKeyInAndStatus(
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
        """,
    )
    fun findPreviewMediaByUserInfoIdAndDiaryIdsInAndStatus(
        @Param("userInfoId") userInfoId: UUID,
        @Param("diaryIds") diaryIds: List<UUID>,
        @Param("status") status: DiaryMediaStatus,
    ): List<DiaryMediaEntity>
}
