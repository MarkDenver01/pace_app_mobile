package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.StudentAssessmentResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import javax.inject.Inject

class GetStudentAssessmentUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(universityId: Long, email: String): NetworkResult<StudentAssessmentResponse> {
        return repository.getStudentAssessment(universityId,  email)
    }
}