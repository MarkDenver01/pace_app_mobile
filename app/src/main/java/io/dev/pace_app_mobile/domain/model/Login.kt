package io.dev.pace_app_mobile.domain.model


/**
 * Login request
 *
 * @property email
 * @property password
 * @constructor Create empty Login request
 */
data class LoginRequest(
    val email: String,
    val password: String
    
)

/**
 * Login response
 *
 * @property username
 * @property role
 * @property jwtToken
 * @property studentResponse
 * @constructor Create empty Login response
 */
data class LoginResponse(
    val username: String,
    val role: String,
    val jwtToken: String,
    val studentResponse: StudentResponse
)

/**
 * Login result
 *
 * @property loginResponse
 * @property statusCode
 * @constructor Create empty Login result
 */
data class LoginResult(
    val loginResponse: LoginResponse?,
    val statusCode: Int,
)