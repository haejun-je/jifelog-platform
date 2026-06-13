package com.jifelog.platform.common.exception

/**
 * 에러 심각도/카테고리를 나타내는 도메인 수준 상태 코드.
 * 웹 계층에서 HttpStatus로 매핑하여 사용한다.
 */
enum class ErrorStatus(val code: Int) {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500),
}