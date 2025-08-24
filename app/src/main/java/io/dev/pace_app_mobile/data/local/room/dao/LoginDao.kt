package io.dev.pace_app_mobile.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.dev.pace_app_mobile.data.local.room.entity.LoginEntity

@Dao
interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoginResponse(login: LoginEntity)

    @Query("SELECT * FROM login_response LIMIT 1")
    suspend fun getLoginResponse(): LoginEntity?

    @Query("DELETE FROM login_response")
    suspend fun clearLogin()

    @Query("SELECT universityId FROM login_response WHERE email = :email LIMIT 1")
    suspend fun getUniversityId(email: String?): Long?

    // Returns the LoginEntity if it exists, otherwise null
    @Query("SELECT * FROM login_response WHERE email = :email LIMIT 1")
    suspend fun getAccountByEmail(email: String): LoginEntity?

    // Returns true if account exists, false otherwise
    suspend fun isAccountExist(email: String): Boolean {
        return getAccountByEmail(email) != null
    }

    @Query("""
        UPDATE login_response
        SET userName = :userName,
            jwtToken = :jwtToken,
            role = :role
        WHERE email = :email
    """)
    suspend fun updateLoginByEmail(email: String, userName: String, jwtToken: String, role: String)
}
