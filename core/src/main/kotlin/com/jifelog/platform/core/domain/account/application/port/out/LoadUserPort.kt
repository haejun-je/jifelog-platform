package com.jifelog.platform.core.domain.account.application.port.out

import com.jifelog.platform.core.domain.account.model.User
import java.util.UUID

interface LoadUserPort {
    fun loadUser(id: UUID): User?
}
