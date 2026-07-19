package com.jifelog.platform.core.config.minio

import io.minio.MinioClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MinioProperties::class)
class MinioConfig {

    @Bean
    fun minioClient(properties: MinioProperties): MinioClient {
        return MinioClient.builder()
            .endpoint(properties.endpoint)
            .credentials(properties.accessKey, properties.secretKey)
            .build()
    }
}