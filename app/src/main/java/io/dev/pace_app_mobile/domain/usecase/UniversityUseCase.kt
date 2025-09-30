package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class UniversityUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke() : Result<List<UniversityResponse>> {
        return repository.getUniversities()
    }

    suspend fun invoke(universityId: Long) : Result<UniversityResponse> {
        return repository.getUniversityById(universityId)
    }
}