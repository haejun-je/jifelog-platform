package com.jifelog.platform.core.domain.diary.application.port.`in`

import java.util.UUID

interface DiaryCommandUseCase {
    fun create(command: CreateDiaryCommand): UUID
    fun delete(id: UUID, userInfoId: UUID)
}