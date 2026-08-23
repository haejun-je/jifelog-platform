package com.jifelog.platform.api.config.logging

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
class RequestResponseLoggingConfig {

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter =
        object : CommonsRequestLoggingFilter() {
            override fun shouldLog(request: HttpServletRequest): Boolean =
                !request.requestURI.startsWith("/actuator/")

            init {
                isIncludePayload = true
                maxPayloadLength = 10_000
                isIncludeQueryString = true
                isIncludeHeaders = true
                isIncludeClientInfo = true
            }
        }
}
