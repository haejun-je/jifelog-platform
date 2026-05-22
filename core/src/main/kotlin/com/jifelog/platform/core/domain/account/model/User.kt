package com.jifelog.platform.core.domain.account.model

import com.fasterxml.uuid.Generators
import java.time.Instant
import java.util.UUID

class User(
    val id: UUID,
    val nickname: String,
    val username: String,
    val profileImg: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun withoutId(
            username: String = "",
            nickname: String,
            profileImg: String
        ): User {
            return User(
                id = Generators.timeBasedEpochGenerator().generate(),
                username = username,
                nickname = nickname,
                profileImg = profileImg,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }

        fun withId(
            id: UUID,
            name: String,
            nickname: String,
            profileImg: String,
            createdAt: Instant,
            updatedAt: Instant,
        ): User = User(
            id = id,
            username = name,
            nickname = nickname,
            profileImg = profileImg,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
