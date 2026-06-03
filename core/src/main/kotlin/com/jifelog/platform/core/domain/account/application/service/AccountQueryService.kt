package com.jifelog.platform.core.domain.account.application.service

import com.jifelog.platform.core.domain.account.application.port.`in`.AccountNotFoundException
import com.jifelog.platform.core.domain.account.application.port.`in`.AccountQueryUseCase
import com.jifelog.platform.core.domain.account.application.port.out.LoadUserPort
import com.jifelog.platform.core.domain.account.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class AccountQueryService(
    private val loadUserPort: LoadUserPort,
) : AccountQueryUseCase {

    override fun getMe(userId: UUID): User =
        loadUserPort.loadUser(userId) ?: throw AccountNotFoundException(userId)
}
