package com.jifelog.platform.core.domain.storage.infrastructure.mapper

import com.jifelog.platform.core.domain.storage.infrastructure.entity.DiaryMediaEntity
import com.jifelog.platform.core.domain.storage.model.DiaryMedia
import com.jifelog.platform.core.domain.storage.model.FileMetadata

object DiaryMediaMapper {
    fun toEntity(media: DiaryMedia): DiaryMediaEntity = DiaryMediaEntity(
        id = media.id,
        diaryId = media.diaryId,
        userInfoId = media.userInfoId,
        bucketName = media.bucketName,
        objectKey = media.objectKey,
        originalName = media.originalName,
        mimeType = media.metadata?.mimeType,
        fileSize = media.metadata?.size,
        etag = media.metadata?.etag,
        sortOrder = media.sortOrder,
        status = media.status,
        createdAt = media.createdAt,
        deletedAt = media.deletedAt,
    )

    fun toDomain(entity: DiaryMediaEntity): DiaryMedia = DiaryMedia.withId(
        id = entity.id,
        diaryId = entity.diaryId,
        userInfoId = entity.userInfoId,
        bucketName = entity.bucketName,
        objectKey = entity.objectKey,
        originalName = entity.originalName,
        // 3 컬럼이 모두 null 인 경우 (= PENDING 처럼 stat 결과를 한 번도 못 받은 row)
        // aggregate 의 metadata 자체가 null 이다. 그 외에는 디폴트("" / 0L / "") 로 채워 VO 를 만든다.
        metadata = if (
            entity.mimeType != null || entity.fileSize != null || entity.etag != null
        ) {
            FileMetadata(
                etag = entity.etag ?: "",
                size = entity.fileSize ?: 0L,
                mimeType = entity.mimeType ?: "",
            )
        } else {
            null
        },
        sortOrder = entity.sortOrder,
        status = entity.status,
        createdAt = entity.createdAt,
        deletedAt = entity.deletedAt,
    )
}
