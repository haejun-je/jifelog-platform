package com.jifelog.platform.core.domain.storage.application.service

import com.jifelog.platform.core.domain.storage.application.port.`in`.DiaryMediaPreviewUseCase
import com.jifelog.platform.core.domain.storage.application.port.out.GenerateDownloadUrlPort
import com.jifelog.platform.core.domain.storage.application.port.out.LoadDiaryMediaPort
import com.jifelog.platform.core.domain.storage.model.DiaryMediaStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 일기 목록 응답용 presigned 미리보기 URL 을 만든다.
 *
 * 책임:
 * 1. 주어진 diaryId 목록에 대한 미리보기 미디어(sort_order = 0, COMMITTED) 를 한 번에 조회한다 (N+1 회피).
 * 2. 각 미디어에 대한 presigned GET URL 을 발급한다.
 *
 * 리포지토리의 sort_order = 0 조건으로 diaryId 1건당 정확히 1행이므로 groupBy 가 필요 없다.
 * presigned URL 자체는 만료 시점이 짧으므로 캐시하지 않는다.
 */
@Service
class DiaryMediaPreviewService(
    private val loadDiaryMediaPort: LoadDiaryMediaPort,
    private val generateDownloadUrlPort: GenerateDownloadUrlPort,
) : DiaryMediaPreviewUseCase {

    /**
     * @return diaryId -> presigned GET URL. media 가 없는 diaryId 는 매핑에 포함되지 않는다.
     */
    @Transactional(readOnly = true)
    override fun getPreviewsByDiaryIds(
        userInfoId: UUID,
        diaryIds: List<UUID>,
    ): Map<UUID, String> {
        if (diaryIds.isEmpty()) return emptyMap()

        val medias = loadDiaryMediaPort.findAllByUserInfoIdAndDiaryIdsInAndStatus(
            userInfoId = userInfoId,
            diaryIds = diaryIds,
            status = DiaryMediaStatus.COMMITTED,
        )

        return medias.associate { media ->
            val diaryId = requireNotNull(media.diaryId) {
                "COMMITTED media must have diaryId: mediaId=${media.id}"
            }
            diaryId to generateDownloadUrlPort.presignGetObject(
                bucketName = media.bucketName,
                objectKey = media.objectKey,
            )
        }
    }

    /**
     * 여러 diary 의 COMMITTED 미디어 전체에 대한 presigned GET URL 을
     * `sort_order ASC` 순서로 발급해 diaryId 별 리스트로 묶어 반환한다.
     *
     * - N+1 회피: 미디어 전체를 1회 조회한 뒤 diaryId 별로 groupBy 한다.
     * - presigned URL 자체는 만료 시점이 짧으므로 캐시하지 않는다.
     * - 정렬은 리포지토리에서 `(diary_id ASC, sort_order ASC)` 로 보장된다.
     */
    @Transactional(readOnly = true)
    override fun getPreviewListsByDiaryIds(
        userInfoId: UUID,
        diaryIds: List<UUID>,
    ): Map<UUID, List<String>> {
        if (diaryIds.isEmpty()) return emptyMap()

        val medias = loadDiaryMediaPort.findAllCommittedByUserInfoIdAndDiaryIdsIn(
            userInfoId = userInfoId,
            diaryIds = diaryIds,
        )

        return medias
            .groupBy { media ->
                requireNotNull(media.diaryId) {
                    "COMMITTED media must have diaryId: mediaId=${media.id}"
                }
            }
            .mapValues { (_, list) ->
                list.map { media ->
                    generateDownloadUrlPort.presignGetObject(
                        bucketName = media.bucketName,
                        objectKey = media.objectKey,
                    )
                }
            }
    }
}