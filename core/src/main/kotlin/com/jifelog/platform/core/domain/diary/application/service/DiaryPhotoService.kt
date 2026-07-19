package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryPhotoUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.GenerateUploadUrlCommand
import com.jifelog.platform.core.domain.storage.application.port.`in`.StorageUseCase
import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class DiaryPhotoService(
    private val storageUseCase: StorageUseCase,
) : DiaryPhotoUseCase {

    companion object {
        private val ALLOWED_CONTENT_TYPES = setOf(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif",
        )

        private val CONTENT_TYPE_TO_EXTENSION = mapOf(
            "image/jpeg" to "jpg",
            "image/png" to "png",
            "image/webp" to "webp",
            "image/gif" to "gif",
        )
    }

    override fun generateUploadUrl(command: GenerateUploadUrlCommand): UploadUrlResult {
        if (command.contentType !in ALLOWED_CONTENT_TYPES) {
            throw BusinessException(ErrorCode.EB_02_002)
        }

        val extension = CONTENT_TYPE_TO_EXTENSION[command.contentType]
            ?: throw BusinessException(ErrorCode.EB_02_002)

        val objectKey = buildObjectKey(command.userInfoId, command.entryDate, extension)

        return storageUseCase.generateUploadUrl(objectKey, command.contentType)
    }

    private fun buildObjectKey(userInfoId: UUID, entryDate: LocalDate, extension: String): String {
        val fileUuid = UUID.randomUUID()
        return "diary/$userInfoId/$entryDate/$fileUuid.$extension"
    }
}