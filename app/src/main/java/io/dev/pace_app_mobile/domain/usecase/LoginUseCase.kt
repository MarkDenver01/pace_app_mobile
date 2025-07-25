package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.login(email, password)
    }
}