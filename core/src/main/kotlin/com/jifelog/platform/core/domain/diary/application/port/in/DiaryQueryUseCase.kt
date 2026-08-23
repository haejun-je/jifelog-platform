package com.jifelog.platform.core.domain.diary.application.port.`in`

import com.jifelog.platform.core.domain.diary.model.Diary
import java.util.UUID

interface DiaryQueryUseCase {
    fun getDiaries(userInfoId: UUID): List<Diary>

    /**
     * 단건 일기를 조회한다.
     *
     * 일기가 존재하지 않거나, 다른 사용자의 일기인 경우
     * `BusinessException(ErrorCode.EN_02_001)`을 던진다.
     */
    fun getDiary(id: UUID, userInfoId: UUID): Diary
}
