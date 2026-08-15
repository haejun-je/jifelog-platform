package com.jifelog.platform.core.domain.storage.application.port.out

import com.jifelog.platform.core.domain.storage.model.DiaryMedia

interface SaveDiaryMediaPort {
    fun save(media: DiaryMedia): DiaryMedia
}
