package com.jifelog.platform.core.domain.storage.application.port.out

import com.jifelog.platform.core.domain.storage.model.DiaryMedia
import com.jifelog.platform.core.domain.storage.model.DiaryMediaStatus
import java.util.UUID

interface LoadDiaryMediaPort {
    fun findPendingByUserInfoIdAndObjectKeys(
        userInfoId: UUID,
        objectKeys: List<String>,
    ): List<DiaryMedia>

    /**
     * 여러 diary 의 미리보기 미디어(sort_order = 0, status 일치) 를 일괄 조회한다.
     * 결과 정렬은 사용처에 영향을 주지 않으므로 두지 않는다.
     */
    fun findAllByUserInfoIdAndDiaryIdsInAndStatus(
        userInfoId: UUID,
        diaryIds: List<UUID>,
        status: DiaryMediaStatus,
    ): List<DiaryMedia>

    /**
     * 여러 diary 의 COMMITTED 미디어 전체를 `sort_order` 오름차순으로 일괄 조회한다 (N+1 회피).
     *
     * - 단건 조회 응답의 다건 presigned URL 발급에 사용된다.
     * - 같은 일기 내에서는 `sort_order ASC` 순서대로 반환된다.
     * - 사용처에서 diaryId 별로 groupBy 하여 매핑한다.
     */
    fun findAllCommittedByUserInfoIdAndDiaryIdsIn(
        userInfoId: UUID,
        diaryIds: List<UUID>,
    ): List<DiaryMedia>
}
