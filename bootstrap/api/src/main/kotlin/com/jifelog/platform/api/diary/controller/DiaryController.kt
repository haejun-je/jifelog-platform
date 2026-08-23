package com.jifelog.platform.api.diary.controller

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryCommandUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryListPreviewUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.GetDiaryDetailUseCase
import com.jifelog.platform.api.diary.controller.dto.CreateDiaryRequest
import com.jifelog.platform.api.diary.controller.dto.CreateDiaryResponse
import com.jifelog.platform.api.diary.controller.dto.DiaryDetailResponse
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
    private val diaryListPreviewUseCase: DiaryListPreviewUseCase,
    private val getDiaryDetailUseCase: GetDiaryDetailUseCase,
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
        val items = diaryListPreviewUseCase.getPreviews(UUID.fromString(jifelogUser.userId))
        return ResponseEntity.ok(items.map { DiaryListItemResponse.from(it) })
    }

    @GetMapping("/{id}", version = "1")
    fun get(
        @JifelogUser jifelogUser: JifelogUserData,
        @PathVariable id: UUID,
    ): ResponseEntity<DiaryDetailResponse> {
        val detail = getDiaryDetailUseCase.getDetail(id, UUID.fromString(jifelogUser.userId))
        return ResponseEntity.ok(DiaryDetailResponse.from(detail))
    }
}