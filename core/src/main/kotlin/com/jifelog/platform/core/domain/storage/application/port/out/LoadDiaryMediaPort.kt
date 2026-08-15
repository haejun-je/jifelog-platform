package com.jifelog.platform.core.domain.storage.application.port.out

import com.jifelog.platform.core.domain.storage.model.DiaryMedia

interface LoadDiaryMediaPort {
    fun findPendingByUserInfoIdAndObjectKeys(
        userInfoId: java.util.UUID,
        objectKeys: List<String>,
    ): List<DiaryMedia>
}
