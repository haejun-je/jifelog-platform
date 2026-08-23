package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryDetailModel
import com.jifelog.platform.core.domain.diary.application.port.`in`.GetDiaryDetailUseCase
import com.jifelog.platform.core.domain.diary.application.port.out.LoadDiaryPort
import com.jifelog.platform.core.domain.storage.application.port.`in`.DiaryMediaPreviewUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


@Service
class GetDiaryDetailService(
    private val loadDiaryPort: LoadDiaryPort,
    private val diaryMediaPreviewUseCase: DiaryMediaPreviewUseCase,
) : GetDiaryDetailUseCase {

    @Transactional(readOnly = true)
    override fun getDetail(id: UUID, userInfoId: UUID): DiaryDetailModel {
        val diary = loadDiaryPort.loadDiary(id, userInfoId)
            ?: throw BusinessException(ErrorCode.EN_02_001)
        val previewUrls = diaryMediaPreviewUseCase.getPreviewByDiaryId(
            userInfoId = userInfoId,
            diaryId = id,
        )
        return DiaryDetailModel(
            diary = diary,
            imageUrls = previewUrls,
        )
    }
}
