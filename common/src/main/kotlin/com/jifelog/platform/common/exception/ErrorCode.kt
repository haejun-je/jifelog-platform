package com.jifelog.platform.common.exception

/**
 * 에러 코드 네이밍 규칙: E[Category]_[Domain]_[Seq]
 *
 * Category (ErrorStatus):
 *   B = BAD_REQUEST (400)
 *   U = UNAUTHORIZED (401)
 *   F = FORBIDDEN (403) (예약)
 *   N = NOT_FOUND (404)
 *   C = CONFLICT (409)
 *   S = INTERNAL_SERVER_ERROR (500)
 *
 * Domain:
 *   00 = 공통/전역
 *   01 = account
 *   02 = diary
 *   03 = storage
 *   새로운 도메인이 추가되면 다음 번호를 순서대로 사용한다.
 *
 * Seq: (Category, Domain) 조합별로 001부터 시작하는 3자리 순번
 */
enum class ErrorCode(val status: ErrorStatus, val defaultMessage: String) {
    // 400 Bad Request - 공통(00)
    EB_00_001(ErrorStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    EB_00_002(ErrorStatus.BAD_REQUEST, "요청을 읽을 수 없습니다."),

    // 404 Not Found
    EN_01_001(ErrorStatus.NOT_FOUND, "존재하지 않는 계정입니다."),

    // 404 Not Found - diary(02)
    EN_02_001(ErrorStatus.NOT_FOUND, "존재하지 않는 일기입니다."),

    // 400 Bad Request - diary(02)
    EB_02_001(ErrorStatus.BAD_REQUEST, "지원하지 않는 이미지 형식입니다."),
    EB_02_002(ErrorStatus.BAD_REQUEST, "지원하지 않는 콘텐츠 타입입니다. 허용: image/jpeg, image/png, image/webp, image/gif"),

    // 409 Conflict
    EC_01_001(ErrorStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    EC_02_001(ErrorStatus.CONFLICT, "이미 작성된 일기가 존재합니다."),

    // 500 Internal Server Error - 공통(00)
    ES_00_001(ErrorStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // 500 Internal Server Error - storage(03)
    ES_03_001(ErrorStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 URL 생성에 실패했습니다."),
}