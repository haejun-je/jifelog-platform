package com.jifelog.platform.core.domain.diary.application.port.`in`

import com.jifelog.platform.core.domain.diary.model.Diary
import java.util.UUID

interface DiaryQueryUseCase {
    fun getDiaries(userInfoId: UUID): List<Diary>
}