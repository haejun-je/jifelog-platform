package com.jifelog.platform.domain.account.application.port.out

import com.jifelog.platform.domain.account.model.User

interface SaveUserPort {
    fun save(user: User): User
    fun existsByNickname(nickname: String): Boolean
}
