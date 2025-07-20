package io.dev.pace_app_mobile.domain.repository

import io.dev.pace_app_mobile.domain.model.QuestionResponse


interface ApiRepository {
    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun register(
        userName: String,
        email: String,
        roles: Set<String>,
        password: String
    ): Result<String>

    suspend fun getQuestions(): Result<List<QuestionResponse>>
}