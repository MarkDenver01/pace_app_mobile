package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.CustomizationResponse
import io.dev.pace_app_mobile.domain.model.UniversityDomainResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import javax.inject.Inject

class UniversityDomainEmailUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(universityId: Long): NetworkResult<UniversityDomainResponse> {
        return repository.getUniversityDomainEmail(universityId)
    }
}