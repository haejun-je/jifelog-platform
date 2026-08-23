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

    fun findAllCommittedByUserInfoIdAndDiaryId(
        userInfoId: UUID,
        diaryId: UUID,
    ): List<DiaryMedia>
}
