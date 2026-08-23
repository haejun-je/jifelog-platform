package com.jifelog.platform.core.domain.diary.application.service

import com.jifelog.platform.common.exception.BusinessException
import com.jifelog.platform.common.exception.ErrorCode
import com.jifelog.platform.core.domain.diary.application.port.`in`.CreateDiaryCommand
import com.jifelog.platform.core.domain.diary.application.port.`in`.DiaryCommandUseCase
import com.jifelog.platform.core.domain.diary.application.port.out.DeleteDiaryPort
import com.jifelog.platform.core.domain.diary.application.port.out.LoadDiaryPort
import com.jifelog.platform.core.domain.diary.application.port.out.SaveDiaryPort
import com.jifelog.platform.core.domain.diary.model.Diary
import com.jifelog.platform.core.domain.storage.application.port.`in`.CommitDiaryMediaCommand
import com.jifelog.platform.core.domain.storage.application.port.`in`.StorageUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DiaryCommandService(
    private val saveDiaryPort: SaveDiaryPort,
    private val loadDiaryPort: LoadDiaryPort,
    private val deleteDiaryPort: DeleteDiaryPort,
    private val storageUseCase: StorageUseCase,
) : DiaryCommandUseCase {

    override fun create(command: CreateDiaryCommand): UUID {
        if (saveDiaryPort.existsByUserInfoIdAndEntryDate(command.userInfoId, command.entryDate)) {
            throw BusinessException(ErrorCode.EC_02_001)
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
        val saved = saveDiaryPort.save(diary)

        // 첨부 사진이 함께 전달되면 PENDING → COMMITTED 로 승격한다.
        // statObject 검증(HTTP)은 StorageService 내부에서 트랜잭션 밖에 수행되므로,
        // MinIO 응답이 지연되어도 DB 커넥션을 점유하지 않는다.
        if (command.objectKeys.isNotEmpty()) {
            storageUseCase.commitMediaForDiary(
                CommitDiaryMediaCommand(
                    userInfoId = command.userInfoId,
                    diaryId = saved.id,
                    objectKeys = command.objectKeys,
                )
            )
        }

        return saved.id
    }

    @Transactional
    override fun delete(id: UUID, userInfoId: UUID) {
        val diary = loadDiaryPort.loadDiary(id, userInfoId)
            ?: throw BusinessException(ErrorCode.EN_02_001)

        deleteDiaryPort.delete(diary)
    }
}