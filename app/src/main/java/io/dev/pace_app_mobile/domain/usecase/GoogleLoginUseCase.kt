package io.dev.pace_app_mobile.domain.usecase

import android.net.Network
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import io.dev.pace_app_mobile.presentation.utils.getHttpStatus
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(idToken: String, universityId: Long? = null): NetworkResult<LoginResult> {
        return repository.googleLogin(idToken, universityId)
    }
}