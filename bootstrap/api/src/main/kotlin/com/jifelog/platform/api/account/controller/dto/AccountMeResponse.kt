package com.jifelog.platform.api.account.controller.dto

import com.jifelog.platform.core.domain.account.model.User
import java.util.UUID

data class AccountMeResponse(
    val id: UUID,
    val nickname: String,
    val loginId: String,
    val profileImg: String,
) {
    companion object {
        fun from(user: User) = AccountMeResponse(
            id = user.id,
            nickname = user.nickname,
            loginId = user.username,
            profileImg = user.profileImg,
        )
    }
}
