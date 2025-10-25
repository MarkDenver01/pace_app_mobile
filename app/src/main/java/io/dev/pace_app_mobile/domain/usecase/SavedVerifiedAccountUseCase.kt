package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.SharedVerifiedAccount
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import javax.inject.Inject

class SavedVerifiedAccountUseCase @Inject constructor(private val repo: LocalDataStoreRepository) {
    suspend operator fun invoke(data: SharedVerifiedAccount) = repo.saveVerifiedAccount(data)
}