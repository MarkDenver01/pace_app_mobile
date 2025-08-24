package io.dev.pace_app_mobile.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_response")
data class LoginEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val userName: String,
    val jwtToken: String,
    val role: String,
    val universityId: Long? = null,
    val email: String? = null
)