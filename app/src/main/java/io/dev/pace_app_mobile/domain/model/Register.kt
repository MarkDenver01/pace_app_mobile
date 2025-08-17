package io.dev.pace_app_mobile.domain.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val roles: Set<String>, // TODO need to change into string only
    val password: String,
    val universityId: Long
)

data class RegisterResponse(
    val message: String
)