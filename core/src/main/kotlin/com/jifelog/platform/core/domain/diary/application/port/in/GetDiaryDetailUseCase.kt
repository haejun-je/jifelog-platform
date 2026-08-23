package com.jifelog.platform.core.domain.diary.application.port.`in`

import com.jifelog.platform.core.domain.diary.model.Diary
import java.util.UUID

/**
 * 일기 상세 조회 응답용 view model.
 *
 * 도메인 모델 [Diary] 와 별개의 "응답 전용" 객체.
 * - media 가 없는 일기는 [imageUrl] 이 `null` 이다.
 * - controller 의 응답 DTO 변환 입력으로만 사용되며, 도메인 로직에 노출되지 않는다.
 */
data class DiaryDetailModel(
    val diary: Diary,
    val imageUrl: String?,
)

/**
 * 일기 상세 조회 use case.
 *
 * - 내부적으로 [DiaryQueryUseCase] 와 [com.jifelog.platform.core.domain.storage.application.port.`in`.DiaryMediaPreviewUseCase] 를 합성해
 *   diary + presigned URL 을 조합한다.
 * - 일기가 존재하지 않거나, 다른 사용자의 일기인 경우
 *   `BusinessException(ErrorCode.EN_02_001)` 을 던진다 ([DiaryQueryUseCase.getDiary] 와 동일 정책).
 */
interface GetDiaryDetailUseCase {
    fun getDetail(id: UUID, userInfoId: UUID): DiaryDetailModel
}
