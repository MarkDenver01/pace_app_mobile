package io.dev.pace_app_mobile.domain.repository

import io.dev.pace_app_mobile.domain.model.LoginResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
}