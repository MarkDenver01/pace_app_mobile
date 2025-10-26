package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGuestKeyUseCase @Inject constructor(private val repo: LocalDataStoreRepository) {
    operator fun invoke(): Flow<String?> = repo.getGuestKey()
}