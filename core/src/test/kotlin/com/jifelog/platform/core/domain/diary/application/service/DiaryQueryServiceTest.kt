package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.core.domain.diary.application.port.out.LoadDiaryPort
import com.jifelog.platform.core.domain.diary.model.Diary
import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

class DiaryQueryServiceTest {

    private val userId: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")

    private fun diary(entryDate: LocalDate): Diary = Diary.withId(
        id = UUID.randomUUID(),
        userInfoId = userId,
        entryDate = entryDate,
        mood = Mood.HAPPY,
        weather = Weather.SUNNY,
        energyLevel = 3,
        satisfactionLevel = 4,
        keywords = listOf("a"),
        achievement = emptyList(),
        regret = emptyList(),
        content = "c",
        createdAt = Instant.parse("2026-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2026-01-01T00:00:00Z"),
    )

    @Test
    fun `getDiaries returns diaries from port for the given user`() {
        val diaries = listOf(
            diary(LocalDate.of(2026, 8, 15)),
            diary(LocalDate.of(2026, 8, 14)),
        )
        val port = StubPort(diaries)
        val service = DiaryQueryService(port)

        val result = service.getDiaries(userId)

        assertEquals(diaries, result)
        assertEquals(userId, port.lastUserInfoId)
    }

    @Test
    fun `getDiaries returns empty list when port returns empty`() {
        val service = DiaryQueryService(StubPort(emptyList()))

        assertTrue(service.getDiaries(userId).isEmpty())
    }

    private class StubPort(private val diaries: List<Diary>) : LoadDiaryPort {
        var lastUserInfoId: UUID? = null
        override fun loadDiary(id: UUID): Diary? = null
        override fun loadAllDiaries(userInfoId: UUID): List<Diary> {
            lastUserInfoId = userInfoId
            return diaries
        }
    }
}