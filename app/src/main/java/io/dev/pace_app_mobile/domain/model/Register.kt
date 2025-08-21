package io.dev.pace_app_mobile.domain.model


/**
 * Register request
 *
 * @property username
 * @property email
 * @property roles
 * @property password
 * @property universityId
 * @constructor Create empty Register request
 */
data class RegisterRequest(
    val username: String,
    val email: String,
    val roles: Set<String>, // TODO need to change into string only
    val password: String,
    val universityId: Long
)

/**
 * Register response
 *
 * @property message
 * @constructor Create empty Register response
 */
data class RegisterResponse(
    val message: String
)