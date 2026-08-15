package com.jifelog.platform.core.config.minio

import com.jifelog.platform.core.config.minio.properties.MinioProperties
import io.minio.MinioClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MinioProperties::class)
class MinioConfig {

    @Bean
    fun minioClient(properties: MinioProperties): MinioClient {
        val client = MinioClient.builder()
            .endpoint(properties.endpoint)
            .credentials(properties.accessKey, properties.secretKey)
            .build()

        // SDK 기본값은 연결/읽기/쓰기 각 5분 — 지연 발생 시 커넥션/스레드를 오래 점유하므로 축소한다.
        client.setTimeout(
            properties.connectTimeoutMillis,
            properties.writeTimeoutMillis,
            properties.readTimeoutMillis,
        )
        return client
    }
}