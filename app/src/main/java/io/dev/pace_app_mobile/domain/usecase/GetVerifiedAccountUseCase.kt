package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.SharedVerifiedAccount
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetVerifiedAccountUseCase(private val repo: LocalDataStoreRepository) {
    operator fun invoke(): Flow<SharedVerifiedAccount?> = repo.getVerifiedAccount()
}