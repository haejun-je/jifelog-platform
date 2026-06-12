package com.jifelog.platform.core.domain.diary.application.port.`in`

import java.time.LocalDate
import java.util.UUID

class DuplicateDiaryException(userInfoId: UUID, entryDate: LocalDate) 
    : RuntimeException("diary already exists for user $userInfoId on $entryDate")