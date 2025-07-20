package io.dev.pace_app_mobile.data.remote.repository

import android.util.Log
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.local.room.entity.LoginEntity
import io.dev.pace_app_mobile.data.remote.api.ApiService
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.RegisterRequest
import io.dev.pace_app_mobile.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val loginDao: LoginDao,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            if (email.isEmpty() || password.isEmpty()) {
                return  Result.failure(Exception("Login failed: Email address or password cannot be empty"))
            }

            // get the response from api login
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val entity = LoginEntity(
                        userName = body.username,
                        jwtToken = body.jwtToken,
                        role = body.role
                    )
                    tokenManager.saveToken(entity.jwtToken)
                    loginDao.insertLoginResponse(entity)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Login failed: Cannot find the username or password."))
                }
            } else {
                Result.failure(Exception("Login failed: Account not valid."))
            }
        } catch (e: Exception) {
            Log.e("error", "$e")
            Result.failure(Exception("Login failed: Unexpected error occur"))
        }
    }

    override suspend fun register(
        userName: String,
        email: String,
        roles: Set<String>,
        password: String
    ): Result<String> {
        return try {
            if (email.isEmpty() || password.isEmpty() || password.isEmpty()) {
                return  Result.failure(Exception("Register failed: Please input the data field"))
            }

            // get the response from register API
            val response = api.register(RegisterRequest(
                userName,
                email,
                roles,
                password
            ))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.message )
                } else {
                    Result.failure(Exception("Register failed: API issue - request body is empty."))
                }
            } else {
                Result.failure(Exception("Register failed: Registration not successful"))
            }
        } catch (e: Exception) {
            Log.e("error", "$e")
            Result.failure(Exception("Register failed: Unexpected error occur"))
        }
    }

}