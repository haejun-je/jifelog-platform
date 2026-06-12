package com.jifelog.platform.core.domain.diary.infrastructure.entity

import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
@Table(
    schema = "diary",
    name = "diary_entry",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_user_entry_date",
            columnNames = ["user_info_id", "entry_date"],
        ),
    ],
)
class DiaryEntity(
    @Id
    var id: UUID,

    @Column(name = "user_info_id", nullable = false)
    var userInfoId: UUID,

    @Column(name = "entry_date", nullable = false)
    var entryDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var mood: Mood,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var weather: Weather,

    @Column(name = "energy_level", nullable = false)
    var energyLevel: Short,

    @Column(name = "satisfaction_level", nullable = false)
    var satisfactionLevel: Short,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "keywords", nullable = false, columnDefinition = "varchar(50)[]")
    var keywords: List<String> = emptyList(),

    @Column(nullable = false)
    var achievement: String = "",

    @Column(nullable = false)
    var regret: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)