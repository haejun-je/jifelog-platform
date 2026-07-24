package com.jifelog.platform.api.account.controller.dto

import com.jifelog.platform.core.domain.account.application.port.`in`.CreateAccountCommand
import jakarta.validation.constraints.Size

data class CreateAccountRequest(
    @field:Size(min = 1, max = 50, message = "nickname must be 1..50 characters")
    val nickname: String,
    val profileImg: String? = "",
) {
    fun toCommand() = CreateAccountCommand(
        nickname = nickname,
        profileImg = profileImg ?: ""
    )
}
