package com.jifelog.platform.core.domain.diary.application.port.out

import java.util.UUID

interface DeleteDiaryPort {
    fun deleteById(id: UUID)
}