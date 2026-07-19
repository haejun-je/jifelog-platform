package com.jifelog.platform.web.diary.controller

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryCommandUseCase
import com.jifelog.platform.web.diary.controller.dto.CreateDiaryRequest
import com.jifelog.platform.web.diary.controller.dto.CreateDiaryResponse
import com.jifelog.security.jwt.api.JifelogUser
import com.jifelog.security.jwt.api.JifelogUserData
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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
}