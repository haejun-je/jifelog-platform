package com.jifelog.platform.core.domain.account.application.port.out

import com.jifelog.platform.core.domain.account.model.User

interface SaveUserPort {
    fun save(user: User): User
    fun existsByNickname(nickname: String): Boolean
}
