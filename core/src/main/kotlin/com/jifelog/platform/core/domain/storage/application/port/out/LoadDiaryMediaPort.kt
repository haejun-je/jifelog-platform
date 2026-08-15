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
     * 여러 diary 의 COMMITTED 미디어를 일괄 조회한다 (N+1 회피).
     * 결과는 (diary_id ASC, sort_order ASC) 로 정렬되어 반환된다.
     */
    fun findAllByUserInfoIdAndDiaryIdsInAndStatus(
        userInfoId: UUID,
        diaryIds: List<UUID>,
        status: DiaryMediaStatus,
    ): List<DiaryMedia>
}
