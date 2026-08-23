package com.jifelog.platform.core.domain.diary.model

import com.fasterxml.uuid.Generators
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

class Diary(
    val id: UUID,
    val userInfoId: UUID,
    val entryDate: LocalDate,
    val mood: Mood,
    val weather: Weather,
    val energyLevel: Short,
    val satisfactionLevel: Short,
    val keywords: List<String>,
    val achievement: List<String>,
    val regret: List<String>,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant? = null,
) {
    companion object {
        fun withoutId(
            userInfoId: UUID,
            entryDate: LocalDate,
            mood: Mood,
            weather: Weather,
            energyLevel: Short,
            satisfactionLevel: Short,
            keywords: List<String> = emptyList(),
            achievement: List<String> = emptyList(),
            regret: List<String> = emptyList(),
            content: String,
        ): Diary = Diary(
            id = Generators.timeBasedEpochGenerator().generate(),
            userInfoId = userInfoId,
            entryDate = entryDate,
            mood = mood,
            weather = weather,
            energyLevel = energyLevel,
            satisfactionLevel = satisfactionLevel,
            keywords = keywords,
            achievement = achievement,
            regret = regret,
            content = content,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            deletedAt = null,
        )

        fun withId(
            id: UUID,
            userInfoId: UUID,
            entryDate: LocalDate,
            mood: Mood,
            weather: Weather,
            energyLevel: Short,
            satisfactionLevel: Short,
            keywords: List<String>,
            achievement: List<String>,
            regret: List<String>,
            content: String,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?,
        ): Diary = Diary(
            id = id,
            userInfoId = userInfoId,
            entryDate = entryDate,
            mood = mood,
            weather = weather,
            energyLevel = energyLevel,
            satisfactionLevel = satisfactionLevel,
            keywords = keywords,
            achievement = achievement,
            regret = regret,
            content = content,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
        )
    }
}