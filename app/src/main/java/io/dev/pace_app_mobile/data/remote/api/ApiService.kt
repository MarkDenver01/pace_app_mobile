package io.dev.pace_app_mobile.data.remote.api

import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/public/login")
    suspend fun login(
        @Body request: LoginRequest): Response<LoginResponse>
}