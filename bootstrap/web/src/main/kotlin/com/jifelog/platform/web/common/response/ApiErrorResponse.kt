package com.jifelog.platform.web.common.response

import com.jifelog.platform.common.exception.ErrorCode

data class ApiErrorResponse(
    val data: ErrorBody,
) {
    data class ErrorBody(
        val errorCode: String,
        val message: String,
        val details: List<Any>?,
    )

    companion object {
        fun of(errorCode: ErrorCode, message: String = errorCode.defaultMessage, details: List<Any>? = null): ApiErrorResponse =
            ApiErrorResponse(ErrorBody(errorCode.name, message, details))
    }
}
