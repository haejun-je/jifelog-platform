package com.jifelog.platform.web.common.exception

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.web.common.response.ApiErrorResponse
import com.jifelog.platform.web.common.response.ValidationErrorDetail
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity
            .status(ex.errorCode.status.toHttpStatus())
            .body(ApiErrorResponse.of(ex.errorCode, ex.message ?: ex.errorCode.defaultMessage, ex.details))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
        val details = ex.bindingResult.fieldErrors.map { fe ->
            ValidationErrorDetail(fe.field, fe.defaultMessage ?: "Invalid value")
        }
        return ResponseEntity
            .badRequest()
            .body(ApiErrorResponse.of(ErrorCode.EB_00_001, details = details))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ApiErrorResponse> {
        log.warn("Malformed request body", ex)
        return ResponseEntity
            .badRequest()
            .body(ApiErrorResponse.of(ErrorCode.EB_00_002))
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception): ResponseEntity<ApiErrorResponse> {
        log.error("Unhandled exception", ex)
        return ResponseEntity
            .internalServerError()
            .body(ApiErrorResponse.of(ErrorCode.ES_00_001))
    }
}
