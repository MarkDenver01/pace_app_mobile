package io.dev.pace_app_mobile.data.remote.datasource

import io.dev.pace_app_mobile.data.remote.network.ApiService
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.RegisterRequest
import io.dev.pace_app_mobile.domain.model.RegisterResponse
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ApiService
) {

    suspend fun fetchCourseRecommendation(
        answers: List<AnsweredQuestionRequest>
    ): List<CourseRecommendationResponse> {
        val response = api.getCourseRecommendation(
            answers
        )

        if (response.isSuccessful) {
            return response.body().orEmpty()
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        // get the response from API
        val response = api.login(loginRequest)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("empty body response")
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun googleLogin(idToken: String, universityId: Long): Result<LoginResponse> {
        return try {
            val response = api.googleLogin(idToken, universityId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Google login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        val response = api.register(registerRequest)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("empty body response")
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun getQuestions(): List<QuestionResponse> {
        val response = api.getAllQuestions()
        if (response.isSuccessful) {
            return response.body().orEmpty()
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun getUniversities(): List<UniversityResponse> {
        val response = api.getAllUniversity()
        if (response.isSuccessful) {
            return response.body().orEmpty()
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }
}