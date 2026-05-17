package com.jifelog.platform.web.account.controller.dto

import com.jifelog.platform.domain.account.application.port.`in`.CreateAccountCommand

data class CreateAccountRequest(
    val nickname: String,
    val profileImg: String,
) {
    fun toCommand() = CreateAccountCommand(nickname = nickname, profileImg = profileImg)
}
