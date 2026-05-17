package com.jifelog.platform.domain.account.model

import com.github.f4b6a3.uuid.UuidCreator
import java.time.OffsetDateTime
import java.util.UUID

class User(
    val id: UUID,
    val nickname: String,
    val profileImg: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
) {
    companion object {
        fun create(nickname: String, profileImg: String): User {
            require(nickname.isNotBlank() && nickname.length <= 50) { "nickname must be 1..50 characters" }
            require(profileImg.isNotBlank() && profileImg.length <= 50) { "profileImg must be 1..50 characters" }
            return User(
                id = UuidCreator.getTimeOrderedEpoch(),
                nickname = nickname,
                profileImg = profileImg,
            )
        }

        fun restore(
            id: UUID,
            nickname: String,
            profileImg: String,
            createdAt: OffsetDateTime?,
            updatedAt: OffsetDateTime?,
        ): User = User(id = id, nickname = nickname, profileImg = profileImg, createdAt = createdAt, updatedAt = updatedAt)
    }
}
