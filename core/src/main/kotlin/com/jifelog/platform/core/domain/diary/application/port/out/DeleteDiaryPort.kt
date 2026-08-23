package com.jifelog.platform.core.domain.diary.application.port.out

import com.jifelog.platform.core.domain.diary.model.Diary

/**
 * 일기 삭제 포트.
 *
 * 정책: hard delete 가 아닌 soft delete 이다.
 * - 호출 시 diary.deletedAt 에 삭제 시각을 세팅한다.
 * - row 자체는 보존되며, LoadDiaryPort 등 활성 조회 경로에서는 더 이상 보이지 않는다.
 */
interface DeleteDiaryPort {
    fun softDelete(diary: Diary)
}
