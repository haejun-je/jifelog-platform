package com.jifelog.platform.domain.account.infrastructure.adapter

import com.jifelog.platform.domain.account.application.port.out.SaveUserPort
import com.jifelog.platform.domain.account.infrastructure.mapper.UserMapper
import com.jifelog.platform.domain.account.infrastructure.repository.UserJpaRepository
import com.jifelog.platform.domain.account.model.User
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userJpaRepository: UserJpaRepository,
) : SaveUserPort {

    override fun save(user: User): User =
        UserMapper.toDomain(userJpaRepository.save(UserMapper.toEntity(user)))

    override fun existsByNickname(nickname: String): Boolean =
        userJpaRepository.existsByNickname(nickname)
}
