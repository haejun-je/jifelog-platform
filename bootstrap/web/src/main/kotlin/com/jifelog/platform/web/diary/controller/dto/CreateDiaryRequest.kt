package com.jifelog.platform.web.diary.controller.dto

import com.jifelog.platform.core.domain.diary.application.port.`in`.CreateDiaryCommand
import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.UUID

data class CreateDiaryRequest(
    @field:NotNull
    val entryDate: LocalDate,

    @field:NotNull
    val mood: Mood,

    @field:NotNull
    val weather: Weather,

    @field:NotNull @field:Min(1) @field:Max(5)
    val energyLevel: Short,

    @field:NotNull @field:Min(1) @field:Max(5)
    val satisfactionLevel: Short,

    @field:Size(max = 10, message = "keywords must be at most 10 items")
    val keywords: List<@Size(max = 50, message = "each keyword must be at most 50 characters") String> = emptyList(),

    @field:Size(max = 1000, message = "achievement must be at most 1000 characters")
    val achievement: String = "",

    @field:Size(max = 1000, message = "regret must be at most 1000 characters")
    val regret: String = "",

    @field:NotBlank
    val content: String,
) {
    fun toCommand(userInfoId: UUID) = CreateDiaryCommand(
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
    )
}