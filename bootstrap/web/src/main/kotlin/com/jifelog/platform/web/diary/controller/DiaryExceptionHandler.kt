package com.jifelog.platform.web.diary.controller

import com.jifelog.platform.core.domain.diary.application.port.`in`.DuplicateDiaryException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class DiaryExceptionHandler {

    @ExceptionHandler(DuplicateDiaryException::class)
    fun handleDuplicateDiary(ex: DuplicateDiaryException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message ?: "diary already exists")
}