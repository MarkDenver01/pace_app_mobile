package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.VerificationCodeRequest
import io.dev.pace_app_mobile.domain.model.VerificationCodeResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import javax.inject.Inject

class VerifyAccountUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(
        email: String,
        verificationCode: Int
    ): NetworkResult<VerificationCodeResponse> {
        return repository.verifyAccount(email, verificationCode)
    }
}