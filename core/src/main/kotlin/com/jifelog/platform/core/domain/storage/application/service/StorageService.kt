package com.jifelog.platform.core.domain.storage.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.domain.storage.application.port.`in`.StorageUseCase
import com.jifelog.platform.core.domain.storage.application.port.`in`.UploadUrlResult
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateUploadUrlPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StorageService(
    private val generateUploadUrlPort: GenerateUploadUrlPort,
) : StorageUseCase {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun generateUploadUrl(objectKey: String, contentType: String): UploadUrlResult {
        return try {
            generateUploadUrlPort.generateUploadUrl(objectKey, contentType)
        }  catch (e: Exception) {
            throw BusinessException(ErrorCode.ES_03_001, cause = e)
        }
    }
}