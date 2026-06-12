package com.jifelog.platform.core.domain.diary.application.port.`in`

import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import java.time.LocalDate
import java.util.UUID

data class CreateDiaryCommand(
    val userInfoId: UUID,
    val entryDate: LocalDate,
    val mood: Mood,
    val weather: Weather,
    val energyLevel: Short,
    val satisfactionLevel: Short,
    val keywords: List<String>,
    val achievement: String,
    val regret: String,
    val content: String,
)