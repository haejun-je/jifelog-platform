package com.jifelog.platform.api.config.logging

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
class RequestResponseLoggingConfig {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter =
        object : CommonsRequestLoggingFilter() {
            override fun shouldLog(request: HttpServletRequest): Boolean =
                !request.requestURI.startsWith("/actuator/")

            override fun beforeRequest(request: HttpServletRequest, message: String) {
                log.info(message)
            }

            override fun afterRequest(request: HttpServletRequest, message: String) {
                log.info(message)
            }

            init {
                isIncludePayload = true
                maxPayloadLength = 10_000
                isIncludeQueryString = true
                isIncludeHeaders = true
                isIncludeClientInfo = true
            }
        }
}
