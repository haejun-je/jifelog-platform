package com.jifelog.platform.common.exception

class BusinessException(
    val errorCode: ErrorCode,
    message: String = errorCode.defaultMessage,
    val details: List<Any>? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
