package io.dev.pace_app_mobile.domain.model

/**
 * University response
 *
 * @property universityId
 * @property universityName
 * @constructor Create empty University response
 */
data class UniversityLinkResponse(
    val message: String
)

/**
 * University domain response
 *
 * @property domainEmail
 */
data class UniversityDomainResponse(
    val domainEmail: String
)