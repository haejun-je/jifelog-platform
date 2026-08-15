package com.jifelog.platform.api.diary.controller

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryCommandUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryQueryUseCase
import com.jifelog.platform.api.diary.controller.dto.CreateDiaryRequest
import com.jifelog.platform.api.diary.controller.dto.CreateDiaryResponse
import com.jifelog.platform.api.diary.controller.dto.DiaryListItemResponse
import com.jifelog.security.jwt.api.JifelogUser
import com.jifelog.security.jwt.api.JifelogUserData
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/diaries")
class DiaryController(
    private val diaryCommandUseCase: DiaryCommandUseCase,
    private val diaryQueryUseCase: DiaryQueryUseCase,
) {

    @PostMapping(version = "1")
    fun create(
        @JifelogUser jifelogUser: JifelogUserData,
        @Valid @RequestBody request: CreateDiaryRequest,
    ): ResponseEntity<CreateDiaryResponse> {
        val id = diaryCommandUseCase.create(
            request.toCommand(UUID.fromString(jifelogUser.userId))
        )

        return ResponseEntity
            .created(URI.create("/api/v1/diaries/$id"))
            .body(CreateDiaryResponse(id))
    }

    @DeleteMapping("/{id}", version = "1")
    fun delete(
        @JifelogUser jifelogUser: JifelogUserData,
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        diaryCommandUseCase.delete(id, UUID.fromString(jifelogUser.userId))
        return ResponseEntity.noContent().build()
    }

    @GetMapping(version = "1")
    fun list(
        @JifelogUser jifelogUser: JifelogUserData,
    ): ResponseEntity<List<DiaryListItemResponse>> {
        val diaries = diaryQueryUseCase.getDiaries(UUID.fromString(jifelogUser.userId))
        return ResponseEntity.ok(diaries.map { DiaryListItemResponse.from(it) })
    }
}