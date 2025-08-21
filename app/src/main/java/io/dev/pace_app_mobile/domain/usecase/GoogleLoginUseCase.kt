package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(idToken: String, universityId: Long): Result<LoginResult> {
        return repository.googleLogin(idToken, universityId)
    }
}