package io.dev.pace_app_mobile.domain.model


data class
SharedDynamicLink(
    val universityId: Long,
    val dynamicToken: String,
    val isVerified: Boolean
)

data class SharedVerifiedAccount(
    val email: String,
    val verified: Boolean,
)