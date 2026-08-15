package com.jifelog.platform.core.domain.storage.infrastructure.adapter

import com.jifelog.platform.core.domain.storage.application.port.out.ObjectStat
import com.jifelog.platform.core.domain.storage.application.port.out.StatObjectPort
import io.minio.MinioClient
import io.minio.StatObjectArgs
import org.springframework.stereotype.Component

@Component
class MinioStatObjectAdapter(
    private val minioClient: MinioClient,
) : StatObjectPort {

    override fun statObject(bucket: String, objectKey: String): ObjectStat {
        val stat = minioClient.statObject(
            StatObjectArgs.builder()
                .bucket(bucket)
                .`object`(objectKey)
                .build()
        )

        // MinIO SDK: etag() returns "" when header missing; size() returns -1 when missing.
        // Content-Type 헤더가 없으면 null. 비어 보이는 값은 도메인 boundary 에서 디폴트로 치환한다.
        return ObjectStat(
            etag = stat.etag().ifBlank { "" },
            size = stat.size().takeIf { it >= 0L } ?: 0L,
            contentType = stat.headers().get("Content-Type")?.ifBlank { "" } ?: "",
        )
    }
}
