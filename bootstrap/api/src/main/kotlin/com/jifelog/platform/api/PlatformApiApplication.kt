package com.jifelog.platform.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.jifelog.platform.api", "com.jifelog.platform.core"])
class PlatformApiApplication

fun main(args: Array<String>) {
    runApplication<PlatformApiApplication>(*args)
}
