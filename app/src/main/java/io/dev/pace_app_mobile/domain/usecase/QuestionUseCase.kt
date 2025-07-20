package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class QuestionUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(): Result<List<QuestionResponse>> {
        return repository.getQuestions()
    }
}