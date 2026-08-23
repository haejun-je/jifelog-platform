package com.jifelog.platform.core.domain.diary.infrastructure.mapper

import com.jifelog.platform.core.domain.diary.infrastructure.entity.DiaryEntity
import com.jifelog.platform.core.domain.diary.model.Diary

object DiaryMapper {
    fun toEntity(diary: Diary): DiaryEntity = DiaryEntity(
        id = diary.id,
        userInfoId = diary.userInfoId,
        entryDate = diary.entryDate,
        mood = diary.mood,
        weather = diary.weather,
        energyLevel = diary.energyLevel,
        satisfactionLevel = diary.satisfactionLevel,
        keywords = diary.keywords,
        achievement = diary.achievement,
        regret = diary.regret,
        content = diary.content,
        createdAt = diary.createdAt,
        updatedAt = diary.updatedAt,
        deletedAt = diary.deletedAt,
    )

    fun toDomain(entity: DiaryEntity): Diary = Diary.withId(
        id = entity.id,
        userInfoId = entity.userInfoId,
        entryDate = entity.entryDate,
        mood = entity.mood,
        weather = entity.weather,
        energyLevel = entity.energyLevel,
        satisfactionLevel = entity.satisfactionLevel,
        keywords = entity.keywords,
        achievement = entity.achievement,
        regret = entity.regret,
        content = entity.content,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
        deletedAt = entity.deletedAt,
    )
}