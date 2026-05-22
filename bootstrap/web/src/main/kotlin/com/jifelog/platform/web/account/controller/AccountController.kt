package com.jifelog.platform.web.account.controller

import com.jifelog.platform.core.domain.account.application.port.`in`.AccountCommandUseCase
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

@RestController
@RequestMapping("/{version}/accounts")
class AccountController(
    private val accountCommandUseCase: AccountCommandUseCase,
) {

    @PostMapping(version = "1")
    fun create(@RequestBody request: CreateAccountRequest): ResponseEntity<CreateAccountResponse> {
        val id = accountCommandUseCase.create(request.toCommand())

        return ResponseEntity
            .created(URI.create("/{version}/accounts/$id"))
            .body(CreateAccountResponse(id))
    }

    @GetMapping(version = "1", path = ["/me"])
    fun me(
        @JifelogUser jifelogUser: JifelogUserData
    ): ResponseEntity<String> {
        return ResponseEntity
            .ok(jifelogUser.toString())
    }
}
