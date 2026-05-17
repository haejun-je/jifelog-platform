package com.jifelog.platform.domain.account.application.port.`in`

import java.util.UUID

interface AccountCommandUseCase {
    fun create(command: CreateAccountCommand): UUID
}