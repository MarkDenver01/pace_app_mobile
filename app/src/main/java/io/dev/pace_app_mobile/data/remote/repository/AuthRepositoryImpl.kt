package io.dev.pace_app_mobile.data.remote.repository

import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.remote.api.ApiService
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
): AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return  try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    tokenManager.saveToken(it.jwtToken) // Save JWT
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}