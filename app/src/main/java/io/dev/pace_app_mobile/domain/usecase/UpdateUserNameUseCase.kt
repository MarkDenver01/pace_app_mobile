package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import jakarta.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val repository: ApiRepository) {
    suspend operator fun invoke(userName: String, email: String): NetworkResult<Map<String, String>> {
        return repository.updateUserName(userName, email)
    }
}