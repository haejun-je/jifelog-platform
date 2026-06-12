package com.jifelog.platform.core.domain.account.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.domain.account.application.port.`in`.AccountCommandUseCase
import com.jifelog.platform.core.domain.account.application.port.`in`.CreateAccountCommand
import com.jifelog.platform.core.domain.account.application.port.out.SaveUserPort
import com.jifelog.platform.core.domain.account.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class AccountCommandService(
    private val saveUserPort: SaveUserPort,
) : AccountCommandUseCase {

    override fun create(command: CreateAccountCommand): UUID {
        if (saveUserPort.existsByNickname(command.nickname)) {
            throw BusinessException(ErrorCode.EC_01_001)
        }
        val user = User.withoutId(
            nickname = command.nickname,
            profileImg = command.profileImg
        )

        return saveUserPort.save(user).id
    }
}
