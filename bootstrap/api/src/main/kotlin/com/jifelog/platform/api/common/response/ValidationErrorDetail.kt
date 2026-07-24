package com.jifelog.platform.api.common.response

data class ValidationErrorDetail(
    val field: String,
    val message: String,
)
