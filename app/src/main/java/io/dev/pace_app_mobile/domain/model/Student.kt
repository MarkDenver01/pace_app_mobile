package io.dev.pace_app_mobile.domain.model

import java.time.LocalDateTime


/**
 * Student response
 *
 * @property studentId
 * @property userName
 * @property email
 * @property requestedDate
 * @property userAccountStatus
 * @property universityId
 * @property universityName
 * @constructor Create empty Student response
 */
data class StudentResponse(
    val studentId: Long,
    val userName: String,
    val email: String,
    val requestedDate: String,
    val userAccountStatus: String,
    val universityId: Long,
    val universityName: String
)