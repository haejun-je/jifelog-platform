package com.jifelog.platform.core.domain.account.application.port.`in`

import com.jifelog.platform.core.domain.account.model.User
import java.util.UUID

interface AccountQueryUseCase {
    fun getMe(userId: UUID): User
}