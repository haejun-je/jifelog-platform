package com.jifelog.platform.core.domain.account.infrastructure.adapter

import com.jifelog.platform.core.domain.account.application.port.out.LoadUserPort
import com.jifelog.platform.core.domain.account.application.port.out.SaveUserPort
import com.jifelog.platform.core.domain.account.infrastructure.mapper.UserMapper
import com.jifelog.platform.core.domain.account.infrastructure.repository.UserJpaRepository
import com.jifelog.platform.core.domain.account.model.User
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserAdapter(
    private val userJpaRepository: UserJpaRepository,
) : SaveUserPort, LoadUserPort {

    override fun save(user: User): User =
        UserMapper.toDomain(
            userJpaRepository.save(
                UserMapper.toEntity(user)
            )
        )

    override fun existsByNickname(nickname: String): Boolean =
        userJpaRepository.existsByNickname(nickname)

    override fun loadUser(id: UUID): User? =
        userJpaRepository.findById(id).map(UserMapper::toDomain).orElse(null)
}
