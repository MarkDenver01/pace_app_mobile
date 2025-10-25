package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import javax.inject.Inject

class SaveDynamicLinkUseCase @Inject constructor(private val repo: LocalDataStoreRepository) {
    suspend operator fun invoke(data: SharedDynamicLink) = repo.saveDynamicLink(data)
}