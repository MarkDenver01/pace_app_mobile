package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.repository.DynamicLinkRepository
import kotlinx.coroutines.flow.Flow

class GetDynamicLinkUseCase(private val repo: DynamicLinkRepository) {
    operator fun invoke(): Flow<SharedDynamicLink?> = repo.getDynamicLink()
}