package io.dev.pace_app_mobile.domain.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val username: String,
    val role: String,
    val jwtToken: String
)