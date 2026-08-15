package com.jifelog.platform.core.domain.storage.infrastructure.adapter

import com.jifelog.platform.core.domain.storage.application.port.out.LoadDiaryMediaPort
import com.jifelog.platform.core.domain.storage.application.port.out.SaveDiaryMediaPort
import com.jifelog.platform.core.domain.storage.infrastructure.mapper.DiaryMediaMapper
import com.jifelog.platform.core.domain.storage.infrastructure.repository.DiaryMediaJpaRepository
import com.jifelog.platform.core.domain.storage.model.DiaryMedia
import com.jifelog.platform.core.domain.storage.model.DiaryMediaStatus
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DiaryMediaAdapter(
    private val diaryMediaJpaRepository: DiaryMediaJpaRepository,
) : SaveDiaryMediaPort, LoadDiaryMediaPort {

    override fun save(media: DiaryMedia): DiaryMedia =
        DiaryMediaMapper.toDomain(
            diaryMediaJpaRepository.save(DiaryMediaMapper.toEntity(media))
        )

    override fun findPendingByUserInfoIdAndObjectKeys(
        userInfoId: UUID,
        objectKeys: List<String>,
    ): List<DiaryMedia> =
        diaryMediaJpaRepository
            .findAllByUserInfoIdAndObjectKeyInAndStatus(
                userInfoId = userInfoId,
                objectKeys = objectKeys,
                status = DiaryMediaStatus.PENDING,
            )
            .map(DiaryMediaMapper::toDomain)
}
