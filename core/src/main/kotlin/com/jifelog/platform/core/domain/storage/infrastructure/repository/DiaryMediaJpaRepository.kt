package com.jifelog.platform.core.domain.storage.infrastructure.repository

import com.jifelog.platform.core.domain.storage.infrastructure.entity.DiaryMediaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DiaryMediaJpaRepository : JpaRepository<DiaryMediaEntity, UUID>
