package com.jifelog.platform.core.domain.diary.infrastructure.repository

import com.jifelog.platform.core.domain.diary.infrastructure.entity.DiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface DiaryJpaRepository : JpaRepository<DiaryEntity, UUID> {
    fun existsByUserInfoIdAndEntryDate(userInfoId: UUID, entryDate: LocalDate): Boolean

    fun findByIdAndUserInfoId(id: UUID, userInfoId: UUID): DiaryEntity?

    fun findAllByUserInfoIdOrderByEntryDateDesc(userInfoId: UUID): List<DiaryEntity>
}