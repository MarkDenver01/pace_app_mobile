package io.dev.pace_app_mobile.domain.model


data class VerificationCodeRequest(
    val email: String,
)

data class VerificationCodeResponse(
    val message: String
)