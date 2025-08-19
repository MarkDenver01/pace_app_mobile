package io.dev.pace_app_mobile.domain.model

import java.time.LocalDateTime

data class StudentResponse(
    val studentId: Long,
    val userName: String,
    val email: String,
    val requestedDate: LocalDateTime,
    val userAccountStatus: String,
    val universityId: Long,
    val universityName: String
)