package com.jifelog.platform.core.domain.storage.infrastructure.entity

import com.jifelog.platform.core.domain.storage.model.DiaryMediaStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(
    schema = "storage",
    name = "diary_media",
)
class DiaryMediaEntity(
    @Id
    var id: UUID,

    @Column(name = "fk_diary_id", nullable = true)
    var diaryId: UUID?,

    @Column(name = "fk_user_id", nullable = false)
    var userInfoId: UUID,

    @Column(name = "bucket_name", nullable = false, length = 100)
    var bucketName: String,

    @Column(name = "object_key", nullable = false, length = 512)
    var objectKey: String,

    @Column(name = "original_name", nullable = false, length = 255)
    var originalName: String,

    @Column(name = "mime_type", nullable = true, length = 100)
    var mimeType: String?,

    @Column(name = "file_size", nullable = true)
    var fileSize: Long?,

    @Column(name = "etag", nullable = true, length = 128)
    var etag: String?,

    @Column(name = "sort_order", nullable = false)
    var sortOrder: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: DiaryMediaStatus,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant,

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: Instant?,
)
