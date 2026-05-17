package com.jifelog.platform.domain.account.application.port.`in`

data class CreateAccountCommand(
    val nickname: String,
    val profileImg: String,
)
