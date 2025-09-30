package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.UniversityLinkResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import net.openid.appauth.TokenResponse
import javax.inject.Inject

class DynamicLinkValidationUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(token: String): NetworkResult<UniversityLinkResponse> {
        return repository.validateDynamicLink(token)
    }
}