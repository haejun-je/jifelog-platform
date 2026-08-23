package com.jifelog.platform.core.domain.diary.application.port.out

import com.jifelog.platform.core.domain.diary.model.Diary
import java.util.UUID

interface LoadDiaryPort {
    fun loadDiary(id: UUID, userInfoId: UUID): Diary?
    fun loadAllDiaries(userInfoId: UUID): List<Diary>
}
