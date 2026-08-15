package com.jifelog.platform.api.diary.controller.dto

import com.jifelog.platform.core.domain.diary.model.Diary
import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import java.time.LocalDate

data class DiaryListItemResponse(
    val date: LocalDate,
    val mood: Mood,
    val weather: Weather,
    val satisfaction: Short,
    val keywords: List<String>,
    val image: String,
) {
    companion object {
        fun from(diary: Diary): DiaryListItemResponse = DiaryListItemResponse(
            date = diary.entryDate,
            mood = diary.mood,
            weather = diary.weather,
            satisfaction = diary.satisfactionLevel,
            keywords = diary.keywords,
            image = "https://mock.local/diary/${diary.id}/preview",
        )
    }
}