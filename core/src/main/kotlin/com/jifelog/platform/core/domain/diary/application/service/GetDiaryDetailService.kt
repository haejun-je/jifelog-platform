package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryDetailModel
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryQueryUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.GetDiaryDetailUseCase
import com.jifelog.platform.core.domain.storage.application.port.`in`.DiaryMediaPreviewUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 일기 상세 응답 조립 전담 service.
 *
 * - [DiaryQueryUseCase.getDiary] 로 일기를 조회하고 (없거나 소유자가 다르면 `EN_02_001`),
 * - [DiaryMediaPreviewUseCase.getPreviewsByDiaryIds] 로 미리보기 presigned URL 을 조회한 뒤,
 * - 두 결과를 [DiaryDetailModel] 로 조합해 반환한다.
 *
 * 컨트롤러는 이 service 의 use case 만 호출하면 된다 (조합 정책은 application layer 내부).
 */
@Service
class GetDiaryDetailService(
    private val diaryQueryUseCase: DiaryQueryUseCase,
    private val diaryMediaPreviewUseCase: DiaryMediaPreviewUseCase,
) : GetDiaryDetailUseCase {

    @Transactional(readOnly = true)
    override fun getDetail(id: UUID, userInfoId: UUID): DiaryDetailModel {
        val diary = diaryQueryUseCase.getDiary(id, userInfoId)
        val previews = diaryMediaPreviewUseCase.getPreviewsByDiaryIds(
            userInfoId = userInfoId,
            diaryIds = listOf(id),
        )
        return DiaryDetailModel(diary = diary, imageUrl = previews[id])
    }
}
