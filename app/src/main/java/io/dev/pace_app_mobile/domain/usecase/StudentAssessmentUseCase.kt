package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.CustomizationResponse
import io.dev.pace_app_mobile.domain.model.StudentAssessmentRequest
import io.dev.pace_app_mobile.domain.model.StudentAssessmentResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import javax.inject.Inject

class StudentAssessmentUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(studentAssessmentRequest: StudentAssessmentRequest): Result<StudentAssessmentResponse> {
        return repository.saveStudentAssessment(studentAssessmentRequest)
    }
}