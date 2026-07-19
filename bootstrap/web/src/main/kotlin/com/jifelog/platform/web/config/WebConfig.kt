package com.jifelog.platform.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.method.HandlerTypePredicate
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val jifelogUserArgumentResolver: HandlerMethodArgumentResolver
) : WebMvcConfigurer {
    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer
            .usePathSegment(1)
            .addSupportedVersions("1", "2")
            .setDefaultVersion("1")
    }

    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        configurer.addPathPrefix(
            "/api/{version}",
            HandlerTypePredicate.forAnnotation(RestController::class.java)
        )
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(jifelogUserArgumentResolver)
    }
}