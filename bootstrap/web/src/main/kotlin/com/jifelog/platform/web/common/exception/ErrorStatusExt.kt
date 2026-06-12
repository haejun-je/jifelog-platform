package com.jifelog.platform.web.common.exception

import com.jifelog.platform.common.exception.ErrorStatus
import org.springframework.http.HttpStatus

fun ErrorStatus.toHttpStatus(): HttpStatus = when (this) {
    ErrorStatus.BAD_REQUEST           -> HttpStatus.BAD_REQUEST
    ErrorStatus.UNAUTHORIZED          -> HttpStatus.UNAUTHORIZED
    ErrorStatus.FORBIDDEN             -> HttpStatus.FORBIDDEN
    ErrorStatus.NOT_FOUND             -> HttpStatus.NOT_FOUND
    ErrorStatus.CONFLICT              -> HttpStatus.CONFLICT
    ErrorStatus.INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
}