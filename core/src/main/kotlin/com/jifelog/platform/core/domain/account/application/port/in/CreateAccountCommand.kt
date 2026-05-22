package com.jifelog.platform.core.domain.account.application.port.`in`

data class CreateAccountCommand(
    val nickname: String,
    val profileImg: String,
)
