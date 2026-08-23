package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.domain.diary.application.port.out.LoadDiaryPort
import com.jifelog.platform.core.domain.diary.model.Diary
import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

class DiaryQueryServiceTest {

    private val userId: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val otherUserId: UUID = UUID.fromString("22222222-2222-2222-2222-222222222222")

    private fun diary(
        id: UUID = UUID.randomUUID(),
        ownerId: UUID = userId,
        entryDate: LocalDate = LocalDate.of(2026, 8, 15),
    ): Diary = Diary.withId(
        id = id,
        userInfoId = ownerId,
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
            diary(entryDate = LocalDate.of(2026, 8, 15)),
            diary(entryDate = LocalDate.of(2026, 8, 14)),
        )
        val port = StubPort(loadAllResult = diaries)
        val service = DiaryQueryService(port)

        val result = service.getDiaries(userId)

        assertEquals(diaries, result)
        assertEquals(userId, port.lastUserInfoId)
    }

    @Test
    fun `getDiaries returns empty list when port returns empty`() {
        val service = DiaryQueryService(StubPort(loadAllResult = emptyList()))

        assertTrue(service.getDiaries(userId).isEmpty())
    }

    @Test
    fun `getDiary returns diary when it belongs to the requester`() {
        val diaryId = UUID.randomUUID()
        val target = diary(id = diaryId, ownerId = userId)
        val port = StubPort(loadByIdResult = target)
        val service = DiaryQueryService(port)

        val result = service.getDiary(diaryId, userId)

        assertEquals(target, result)
        assertEquals(diaryId, port.lastLoadId)
    }

    @Test
    fun `getDiary throws EN_02_001 when diary does not exist`() {
        val diaryId = UUID.randomUUID()
        val port = StubPort(loadByIdResult = null)
        val service = DiaryQueryService(port)

        val exception = assertThrows(BusinessException::class.java) {
            service.getDiary(diaryId, userId)
        }
        assertEquals(ErrorCode.EN_02_001, exception.errorCode)
    }

    @Test
    fun `getDiary throws EN_02_001 when diary belongs to another user`() {
        val diaryId = UUID.randomUUID()
        val target = diary(id = diaryId, ownerId = otherUserId)
        val port = StubPort(loadByIdResult = target)
        val service = DiaryQueryService(port)

        val exception = assertThrows(BusinessException::class.java) {
            service.getDiary(diaryId, userId)
        }
        assertEquals(ErrorCode.EN_02_001, exception.errorCode)
    }

    private class StubPort(
        private val loadAllResult: List<Diary> = emptyList(),
        private val loadByIdResult: Diary? = null,
    ) : LoadDiaryPort {
        var lastUserInfoId: UUID? = null
        var lastLoadId: UUID? = null

        override fun loadDiary(id: UUID): Diary? {
            lastLoadId = id
            return loadByIdResult
        }

        override fun loadAllDiaries(userInfoId: UUID): List<Diary> {
            lastUserInfoId = userInfoId
            return loadAllResult
        }
    }
}
