package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.core.domain.diary.application.port.`in`.CreateDiaryCommand
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryCommandUseCase
import com.jifelog.platform.core.domain.diary.application.port.`in`.DuplicateDiaryException
import com.jifelog.platform.core.domain.diary.application.port.out.SaveDiaryPort
import com.jifelog.platform.core.domain.diary.model.Diary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class DiaryCommandService(
    private val saveDiaryPort: SaveDiaryPort,
) : DiaryCommandUseCase {

    override fun create(command: CreateDiaryCommand): UUID {
        if (saveDiaryPort.existsByUserInfoIdAndEntryDate(command.userInfoId, command.entryDate)) {
            throw DuplicateDiaryException(command.userInfoId, command.entryDate)
        }
        val diary = Diary.withoutId(
            userInfoId = command.userInfoId,
            entryDate = command.entryDate,
            mood = command.mood,
            weather = command.weather,
            energyLevel = command.energyLevel,
            satisfactionLevel = command.satisfactionLevel,
            keywords = command.keywords,
            achievement = command.achievement,
            regret = command.regret,
            content = command.content,
        )

        return saveDiaryPort.save(diary).id
    }
}