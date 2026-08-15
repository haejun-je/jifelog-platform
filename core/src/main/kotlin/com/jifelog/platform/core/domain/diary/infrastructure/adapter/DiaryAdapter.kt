package com.jifelog.platform.core.domain.diary.infrastructure.adapter

import com.jifelog.platform.core.domain.diary.application.port.out.DeleteDiaryPort
import com.jifelog.platform.core.domain.diary.application.port.out.LoadDiaryPort
import com.jifelog.platform.core.domain.diary.application.port.out.SaveDiaryPort
import com.jifelog.platform.core.domain.diary.infrastructure.mapper.DiaryMapper
import com.jifelog.platform.core.domain.diary.infrastructure.repository.DiaryJpaRepository
import com.jifelog.platform.core.domain.diary.model.Diary
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

@Component
class DiaryAdapter(
    private val diaryJpaRepository: DiaryJpaRepository,
) : SaveDiaryPort, LoadDiaryPort, DeleteDiaryPort {

    override fun save(diary: Diary): Diary =
        DiaryMapper.toDomain(
            diaryJpaRepository.save(
                DiaryMapper.toEntity(diary)
            )
        )

    override fun existsByUserInfoIdAndEntryDate(userInfoId: UUID, entryDate: LocalDate): Boolean =
        diaryJpaRepository.existsByUserInfoIdAndEntryDate(userInfoId, entryDate)

    override fun loadDiary(id: UUID): Diary? =
        diaryJpaRepository.findById(id).map(DiaryMapper::toDomain).orElse(null)

    override fun loadAllDiaries(userInfoId: UUID): List<Diary> =
        diaryJpaRepository.findAllByUserInfoIdOrderByEntryDateDesc(userInfoId).map(DiaryMapper::toDomain)

    override fun deleteById(id: UUID) {
        diaryJpaRepository.deleteById(id)
    }
}