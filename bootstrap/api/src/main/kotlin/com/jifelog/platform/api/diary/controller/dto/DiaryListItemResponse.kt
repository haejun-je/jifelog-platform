package com.jifelog.platform.api.diary.controller.dto

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryPreviewModel
import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import java.time.LocalDate
import java.util.UUID

data class DiaryListItemResponse(
    val id: UUID,
    val date: LocalDate,
    val mood: Mood,
    val weather: Weather,
    val satisfaction: Short,
    val keywords: List<String>,
    val content: String,
    val imageUrl: String?,
) {
    companion object {
        fun from(model: DiaryPreviewModel): DiaryListItemResponse {
            val diary = model.diary
            return DiaryListItemResponse(
                id = diary.id,
                date = diary.entryDate,
                mood = diary.mood,
                weather = diary.weather,
                satisfaction = diary.satisfactionLevel,
                keywords = diary.keywords,
                content = diary.content,
                imageUrl = model.imageUrl,
            )
        }
    }
}