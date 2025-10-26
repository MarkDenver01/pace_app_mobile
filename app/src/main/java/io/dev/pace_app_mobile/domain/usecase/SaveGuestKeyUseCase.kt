package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import javax.inject.Inject

class SaveGuestKeyUseCase @Inject constructor(private val repo: LocalDataStoreRepository) {
    suspend operator fun invoke(message: String) = repo.saveGuestKey(message)
}