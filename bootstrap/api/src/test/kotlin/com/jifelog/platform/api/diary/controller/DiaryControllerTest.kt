package com.jifelog.platform.api.diary.controller

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryCommandUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryQueryUseCase
import com.jifelog.platform.core.domain.diary.model.Diary
import com.jifelog.platform.core.domain.diary.model.Mood
import com.jifelog.platform.core.domain.diary.model.Weather
import com.jifelog.security.jwt.api.JifelogUser
import com.jifelog.security.jwt.api.JifelogUserData
import com.jifelog.security.jwt.config.JwtSecurityAutoConfiguration
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.MethodParameter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@WebMvcTest(DiaryController::class)
@EnableAutoConfiguration(exclude = [JwtSecurityAutoConfiguration::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(DiaryControllerTest.StubConfig::class)
class DiaryControllerTest {

    @MockitoBean
    private lateinit var diaryQueryUseCase: DiaryQueryUseCase

    @MockitoBean
    private lateinit var diaryCommandUseCase: DiaryCommandUseCase

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val userId: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")

    @TestConfiguration
    class StubConfig {
        @Bean
        fun jifelogUserArgumentResolver(): HandlerMethodArgumentResolver =
            object : HandlerMethodArgumentResolver {
                override fun supportsParameter(parameter: MethodParameter): Boolean =
                    parameter.getParameterAnnotation(JifelogUser::class.java) != null

                override fun resolveArgument(
                    parameter: MethodParameter,
                    mavContainer: ModelAndViewContainer?,
                    webRequest: NativeWebRequest,
                    binderFactory: WebDataBinderFactory?,
                ): Any = JifelogUserData(
                    userId = "11111111-1111-1111-1111-111111111111",
                    username = "tester",
                    nickname = "tester",
                )
            }
    }

    @Test
    fun `GET diaries returns list with all fields`() {
        val diaryId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        val diary = Diary.withId(
            id = diaryId,
            userInfoId = userId,
            entryDate = LocalDate.of(2026, 8, 15),
            mood = Mood.HAPPY,
            weather = Weather.SUNNY,
            energyLevel = 3,
            satisfactionLevel = 4,
            keywords = listOf("a", "b"),
            achievement = emptyList(),
            regret = emptyList(),
            content = "c",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
        Mockito.`when`(diaryQueryUseCase.getDiaries(userId)).thenReturn(listOf(diary))

        mockMvc.perform(get("/api/v1/diaries"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].date").value("2026-08-15"))
            .andExpect(jsonPath("$[0].mood").value("HAPPY"))
            .andExpect(jsonPath("$[0].weather").value("SUNNY"))
            .andExpect(jsonPath("$[0].satisfaction").value(4))
            .andExpect(jsonPath("$[0].keywords[0]").value("a"))
            .andExpect(jsonPath("$[0].image").value("https://mock.local/diary/$diaryId/preview"))
    }

    @Test
    fun `GET diaries returns empty array when no diaries`() {
        Mockito.`when`(diaryQueryUseCase.getDiaries(userId)).thenReturn(emptyList())

        mockMvc.perform(get("/api/v1/diaries"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }
}