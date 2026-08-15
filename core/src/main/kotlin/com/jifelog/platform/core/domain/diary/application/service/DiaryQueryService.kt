package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryQueryUseCase
import com.jifelog.platform.core.domain.diary.application.port.out.LoadDiaryPort
import com.jifelog.platform.core.domain.diary.model.Diary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DiaryQueryService(
    private val loadDiaryPort: LoadDiaryPort,
) : DiaryQueryUseCase {

    @Transactional(readOnly = true)
    override fun getDiaries(userInfoId: UUID): List<Diary> =
        loadDiaryPort.loadAllDiaries(userInfoId)
}