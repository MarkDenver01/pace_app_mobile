package io.dev.pace_app_mobile.domain.repository

import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.UniversityResponse


interface ApiRepository {
    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun register(
        userName: String,
        email: String,
        roles: Set<String>,
        password: String,
        universityId: Long
    ): Result<String>

    suspend fun getQuestions(): Result<List<QuestionResponse>>

    suspend fun getUniversities(): Result<List<UniversityResponse>>

    suspend fun getCourseRecommendation(
        answers: List<AnsweredQuestionRequest>
    ): Result<List<CourseRecommendationResponse>>
}