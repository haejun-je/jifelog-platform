package com.jifelog.platform.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val jifelogUserArgumentResolver: HandlerMethodArgumentResolver
) : WebMvcConfigurer {
    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer
            .usePathSegment(0)
            .addSupportedVersions("1", "2")
            .setDefaultVersion("1")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(jifelogUserArgumentResolver)
    }
}