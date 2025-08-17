package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class CourseRecommendationUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(answers: List<AnsweredQuestionRequest>): Result<List<CourseRecommendationResponse>> {
        return repository.getCourseRecommendation(answers)
    }
}