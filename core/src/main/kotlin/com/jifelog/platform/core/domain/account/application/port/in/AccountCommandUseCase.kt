package com.jifelog.platform.core.domain.account.application.port.`in`

import java.util.UUID

interface AccountCommandUseCase {
    fun create(command: CreateAccountCommand): UUID
}