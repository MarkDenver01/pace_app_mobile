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
}