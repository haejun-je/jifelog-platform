package com.jifelog.platform.web.account.controller

import com.jifelog.platform.core.domain.account.application.port.`in`.AccountCommandUseCase
import com.jifelog.platform.core.domain.account.application.port.`in`.AccountQueryUseCase
import com.jifelog.platform.web.account.controller.dto.AccountMeResponse
import com.jifelog.platform.web.account.controller.dto.CreateAccountRequest
import com.jifelog.platform.web.account.controller.dto.CreateAccountResponse
import com.jifelog.security.jwt.api.JifelogUser
import com.jifelog.security.jwt.api.JifelogUserData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountCommandUseCase: AccountCommandUseCase,
    private val accountQueryUseCase: AccountQueryUseCase,
) {

    @PostMapping(version = "1")
    fun create(@RequestBody request: CreateAccountRequest): ResponseEntity<CreateAccountResponse> {
        val id = accountCommandUseCase.create(request.toCommand())

        return ResponseEntity
            .created(URI.create("/api/v1/accounts/$id"))
            .body(CreateAccountResponse(id))
    }

    @GetMapping(version = "1", path = ["/me"])
    fun me(@JifelogUser jifelogUser: JifelogUserData): ResponseEntity<AccountMeResponse> {
        val user = accountQueryUseCase.getMe(
            UUID.fromString(jifelogUser.userId)
        )

        return ResponseEntity.ok(AccountMeResponse.from(user))
    }
}
