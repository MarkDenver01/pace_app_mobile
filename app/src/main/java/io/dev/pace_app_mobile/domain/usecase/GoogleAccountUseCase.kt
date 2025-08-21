package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class GoogleAccountUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return repository.isExistingGoogleAccount(email)
    }
}