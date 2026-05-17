package com.jifelog.platform.domain.account.infrastructure.repository

import com.jifelog.platform.domain.account.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    fun existsByNickname(nickname: String): Boolean
}
