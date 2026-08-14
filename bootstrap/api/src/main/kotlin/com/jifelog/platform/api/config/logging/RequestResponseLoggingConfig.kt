package com.jifelog.platform.api.config.logging

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
class RequestResponseLoggingConfig {

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter =
        CommonsRequestLoggingFilter().apply {
            setIncludePayload(true)
            setMaxPayloadLength(10_000)
            setIncludeQueryString(true)
            setIncludeHeaders(true)
            setIncludeClientInfo(true)
        }
}
