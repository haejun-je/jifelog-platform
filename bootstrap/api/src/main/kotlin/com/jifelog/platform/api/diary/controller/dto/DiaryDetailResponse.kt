package com.jifelog.platform.api.diary.controller.dto

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryDetailModel
import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * 일기 상세 조회 응답 모델.
 *
 * 도메인 모델(`Diary`)과 동일한 필드를 노출하되, 응답 전용 DTO로 분리하여
 * 도메인 변경 없이 응답 스펙을 자유롭게 확장할 수 있도록 한다.
 */
data class DiaryDetailResponse(
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
    val imageUrl: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun from(model: DiaryDetailModel): DiaryDetailResponse {
            val diary = model.diary
            return DiaryDetailResponse(
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
                imageUrl = model.imageUrl,
                createdAt = diary.createdAt,
                updatedAt = diary.updatedAt,
            )
        }
    }
}
