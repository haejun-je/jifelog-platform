package com.jifelog.platform.core.domain.storage.application.port.out

import java.util.UUID

/**
 * diary 미디어 soft delete 포트.
 *
 * 정책: 일기 soft delete 와 동일한 의미. row 자체는 보존되며,
 * LoadDiaryMediaPort 등 활성 조회 경로에서는 더 이상 보이지 않는다.
 *
 * 호출 컨텍스트: 일기 soft delete 와 같은 트랜잭션에서 호출되어야 한다.
 */
interface SoftDeleteDiaryMediaPort {
    fun softDeleteByDiaryId(diaryId: UUID)
}
