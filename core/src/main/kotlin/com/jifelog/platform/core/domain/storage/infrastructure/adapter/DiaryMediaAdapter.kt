package com.jifelog.platform.core.domain.storage.infrastructure.adapter

import com.jifelog.platform.core.domain.storage.application.port.out.SaveDiaryMediaPort
import com.jifelog.platform.core.domain.storage.infrastructure.mapper.DiaryMediaMapper
import com.jifelog.platform.core.domain.storage.infrastructure.repository.DiaryMediaJpaRepository
import com.jifelog.platform.core.domain.storage.model.DiaryMedia
import org.springframework.stereotype.Component

@Component
class DiaryMediaAdapter(
    private val diaryMediaJpaRepository: DiaryMediaJpaRepository,
) : SaveDiaryMediaPort {

    override fun save(media: DiaryMedia): DiaryMedia =
        DiaryMediaMapper.toDomain(
            diaryMediaJpaRepository.save(DiaryMediaMapper.toEntity(media))
        )
}
