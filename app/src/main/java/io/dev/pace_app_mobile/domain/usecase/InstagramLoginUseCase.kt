package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import javax.inject.Inject

class InstagramLoginUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(
        accessToken: String,
        universityId: Long? = null
    ): NetworkResult<LoginResult> {
        return repository.instagramLogin(accessToken, universityId)
    }
}