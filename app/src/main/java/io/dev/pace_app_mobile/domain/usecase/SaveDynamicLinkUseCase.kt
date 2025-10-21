package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.repository.DynamicLinkRepository

class SaveDynamicLinkUseCase(private val repo: DynamicLinkRepository) {
    suspend operator fun invoke(data: SharedDynamicLink) = repo.saveDynamicLink(data)
}