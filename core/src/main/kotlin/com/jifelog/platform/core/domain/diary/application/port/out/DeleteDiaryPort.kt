package com.jifelog.platform.core.domain.diary.application.port.out

import com.jifelog.platform.core.domain.diary.model.Diary

interface DeleteDiaryPort {
    fun delete(diary: Diary)
}
