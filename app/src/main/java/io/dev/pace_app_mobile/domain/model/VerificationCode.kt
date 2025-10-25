package io.dev.pace_app_mobile.domain.model


data class VerificationCodeRequest(
    val email: String,
)

data class VerifyAccountRequest(
    val email: String,
    val verificationCode: Int
)

data class VerificationCodeResponse(
    val message: String
)
