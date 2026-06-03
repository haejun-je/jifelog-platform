package com.jifelog.platform.core.domain.account.application.port.`in`

import java.util.UUID

class AccountNotFoundException(userId: UUID) : RuntimeException("account not found: $userId")
