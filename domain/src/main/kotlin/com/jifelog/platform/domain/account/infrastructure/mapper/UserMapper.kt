package com.jifelog.platform.domain.account.infrastructure.mapper

import com.jifelog.platform.domain.account.infrastructure.entity.UserEntity
import com.jifelog.platform.domain.account.model.User

object UserMapper {
    fun toEntity(user: User): UserEntity = UserEntity(
        id = user.id,
        nickname = user.nickname,
        profileImg = user.profileImg,
    )

    fun toDomain(entity: UserEntity): User = User.restore(
        id = entity.id,
        nickname = entity.nickname,
        profileImg = entity.profileImg,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
    )
}
