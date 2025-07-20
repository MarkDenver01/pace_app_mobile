package io.dev.pace_app_mobile.domain.repository


interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun register(
        userName: String,
        email: String,
        roles: Set<String>,
        password: String): Result<String>
}