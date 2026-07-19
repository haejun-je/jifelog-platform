package com.jifelog.platform.core.config.jpa

import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.jifelog.platform.core"])
@EntityScan(basePackages = ["com.jifelog.platform.core"])
class JpaConfig {
}