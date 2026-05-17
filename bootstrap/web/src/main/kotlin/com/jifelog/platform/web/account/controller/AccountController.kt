package com.jifelog.platform.web.account.controller

import com.jifelog.platform.domain.account.application.port.`in`.AccountCommandUseCase
import com.jifelog.platform.web.account.controller.dto.CreateAccountRequest
import com.jifelog.platform.web.account.controller.dto.CreateAccountResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountCommandUseCase: AccountCommandUseCase,
) {

    @PostMapping
    fun create(@RequestBody request: CreateAccountRequest): ResponseEntity<CreateAccountResponse> {
        val id = accountCommandUseCase.create(request.toCommand())
        return ResponseEntity
            .created(URI.create("/api/accounts/$id"))
            .body(CreateAccountResponse(id))
    }
}
