package com.jifelog.platform.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.jifelog.platform.web", "com.jifelog.platform.core"])
class PlatformApiApplication

fun main(args: Array<String>) {
    runApplication<PlatformApiApplication>(*args)
}
