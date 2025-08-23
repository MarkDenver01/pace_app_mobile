package io.dev.pace_app_mobile.domain.usecase

import com.facebook.AccessToken
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class FacebookAccountUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(accessToken: String): Boolean {
        return repository.isExistingFacebookAccount(accessToken)
    }
}