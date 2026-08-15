package com.jifelog.platform.core.domain.storage.application.port.out

/**
 * MinIO statObject 응답을 도메인 boundary 에서 표현한 transport DTO.
 * 모든 필드는 바운더리에서 디폴트로 채워져 non-null 이다.
 */
data class ObjectStat(
    val etag: String,
    val size: Long,
    val contentType: String,
)

interface StatObjectPort {
    fun statObject(bucket: String, objectKey: String): ObjectStat
}
