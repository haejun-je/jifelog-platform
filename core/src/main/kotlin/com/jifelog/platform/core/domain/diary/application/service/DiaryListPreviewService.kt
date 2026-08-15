package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryListPreviewUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryPreviewModel
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryQueryUseCase
import com.jifelog.platform.core.domain.storage.application.port.`in`.DiaryMediaPreviewUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 일기 목록 응답 조립 전담 service.
 *
 * - [DiaryQueryUseCase] 로 일기 목록을 조회하고,
 * - [DiaryMediaPreviewUseCase] 로 미리보기 presigned URL 을 일괄 조회한 뒤,
 * - 두 결과를 [DiaryPreviewModel] 로 조합해 반환한다.
 *
 * 컨트롤러는 이 service 의 use case 만 호출하면 된다 (조합 정책은 application layer 내부).
 */
@Service
class DiaryListPreviewService(
    private val diaryQueryUseCase: DiaryQueryUseCase,
    private val diaryMediaPreviewUseCase: DiaryMediaPreviewUseCase,
) : DiaryListPreviewUseCase {

    @Transactional(readOnly = true)
    override fun getPreviews(userInfoId: UUID): List<DiaryPreviewModel> {
        val diaries = diaryQueryUseCase.getDiaries(userInfoId)
        if (diaries.isEmpty()) return emptyList()

        val previews = diaryMediaPreviewUseCase.getPreviewsByDiaryIds(
            userInfoId = userInfoId,
            diaryIds = diaries.map { it.id },
        )

        return diaries.map { DiaryPreviewModel(it, previews[it.id]) }
    }
}