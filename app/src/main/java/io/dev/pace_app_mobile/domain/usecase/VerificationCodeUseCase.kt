package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.model.VerificationCodeRequest
import io.dev.pace_app_mobile.domain.model.VerificationCodeResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import javax.inject.Inject

class VerificationCodeUseCase @Inject constructor(
    private val repository: ApiRepository) {

    suspend operator fun invoke(verificationCodeRequest: VerificationCodeRequest) : NetworkResult<VerificationCodeResponse> {
        return repository.sendVerificationCode(verificationCodeRequest)
    }

}