package com.jifelog.platform.domain.config

import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.jifelog.platform.domain"])
@EntityScan(basePackages = ["com.jifelog.platform.domain"])
class JpaConfig {
}