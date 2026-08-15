package com.jifelog.platform.core.domain.storage.infrastructure.mapper

import com.jifelog.platform.core.domain.storage.infrastructure.entity.DiaryMediaEntity
import com.jifelog.platform.core.domain.storage.model.DiaryMedia

object DiaryMediaMapper {
    fun toEntity(media: DiaryMedia): DiaryMediaEntity = DiaryMediaEntity(
        id = media.id,
        diaryId = media.diaryId,
        userInfoId = media.userInfoId,
        bucketName = media.bucketName,
        objectKey = media.objectKey,
        originalName = media.originalName,
        mimeType = media.mimeType,
        fileSize = media.fileSize,
        etag = media.etag,
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
        mimeType = entity.mimeType,
        fileSize = entity.fileSize,
        etag = entity.etag,
        sortOrder = entity.sortOrder,
        status = entity.status,
        createdAt = entity.createdAt,
        deletedAt = entity.deletedAt,
    )
}
