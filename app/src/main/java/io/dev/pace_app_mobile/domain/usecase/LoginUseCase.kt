package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<LoginResponse> {
        return repository.login(email, password)
    }
}