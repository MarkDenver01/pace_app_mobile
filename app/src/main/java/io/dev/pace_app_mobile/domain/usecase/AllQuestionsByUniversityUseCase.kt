package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class AllQuestionsByUniversityUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(): Result<List<QuestionResponse>> {
        return repository.getAllQuestionsByUniversity()
    }
}