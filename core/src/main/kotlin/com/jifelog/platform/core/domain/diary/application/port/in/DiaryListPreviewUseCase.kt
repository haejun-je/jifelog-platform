package com.jifelog.platform.core.domain.diary.application.port.`in`

import com.jifelog.platform.core.domain.diary.model.Diary
import java.util.UUID

/**
 * 일기 목록 응답용 view model.
 *
 * 도메인 모델 [Diary] 와 별개의 "응답 전용" 객체.
 * - media 가 없는 일기는 [imageUrl] 이 `null` 이다.
 * - controller 의 응답 DTO 변환 입력으로만 사용되며, 도메인 로직에 노출되지 않는다.
 */
data class DiaryPreviewModel(
    val diary: Diary,
    val imageUrl: String?,
)

/**
 * 일기 목록 미리보기 조회 use case.
 *
 * - 내부적으로 [DiaryQueryUseCase] 와 `DiaryMediaPreviewUseCase` 를 합성해 diary + presigned URL 을 조합한다.
 * - 클라이언트(컨트롤러)는 이 use case 한 개만 호출하면 된다 (조합 책임은 application layer).
 */
interface DiaryListPreviewUseCase {
    fun getPreviews(userInfoId: UUID): List<DiaryPreviewModel>
}