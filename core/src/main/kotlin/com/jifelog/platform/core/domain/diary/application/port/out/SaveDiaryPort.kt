package com.jifelog.platform.core.domain.diary.application.port.out

import com.jifelog.platform.core.domain.diary.model.Diary
import java.time.LocalDate
import java.util.UUID

interface SaveDiaryPort {
    fun save(diary: Diary): Diary
    fun existsByUserInfoIdAndEntryDate(userInfoId: UUID, entryDate: LocalDate): Boolean
}