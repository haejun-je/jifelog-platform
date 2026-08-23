package com.jifelog.platform.core.domain.storage.application.port.`in`

import java.util.UUID

/**
 * 일기 첨부 미디어의 presigned 미리보기 URL 조회 use case.
 *
 * 목록 응답(예: `GET /api/v1/diaries`) 에서 diary 별 첫 번째 미디어에 대한 presigned GET URL 을
 * 일괄 발급한다. DB 조회는 1회로 제한(N+1 회피) 하고, presign 호출은 media 건수만큼 수행한다.
 */
interface DiaryMediaPreviewUseCase {
    /**
     * @return diaryId -> presigned GET URL. media 가 없는 diaryId 는 매핑에 포함되지 않는다.
     */
    fun getPreviewsByDiaryIds(
        userInfoId: UUID,
        diaryIds: List<UUID>,
    ): Map<UUID, String>

    /**
     * 단건 조회 응답용: 여러 diary 의 COMMITTED 미디어 전체에 대한 presigned GET URL 을
     * `sort_order ASC` 순서대로 발급해 diaryId 별 리스트로 반환한다.
     *
     * @return diaryId -> presigned GET URL 목록. media 가 없는 diaryId 는 매핑에 포함되지 않는다.
     */
    fun getPreviewByDiaryId(
        userInfoId: UUID,
        diaryId: UUID,
    ): List<String>
}