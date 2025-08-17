package io.dev.pace_app_mobile.data.remote.repository

import android.util.Log
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.local.room.entity.LoginEntity
import io.dev.pace_app_mobile.data.remote.datasource.RemoteDataSource
import io.dev.pace_app_mobile.data.remote.network.ApiService
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.RegisterRequest
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val loginDao: LoginDao,
    private val tokenManager: TokenManager
) : ApiRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            if (email.isEmpty() || password.isEmpty()) {
                return Result.failure(Exception("Login failed: Email address or password cannot be empty"))
            }

            val result = remoteDataSource.login(LoginRequest(email, password))

            run {
                val entity = LoginEntity(
                    userName = result.username,
                    jwtToken = result.jwtToken,
                    role = result.role
                )

                tokenManager.saveToken(entity.jwtToken) // TODO maybe modify soon
                loginDao.insertLoginResponse(entity)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Log.e("error", "$e")
            Result.failure(Exception("Login failed: $e"))
        }
    }

    override suspend fun register(
        userName: String,
        email: String,
        roles: Set<String>,
        password: String,
        universityId: Long
    ): Result<String> {
        return try {
            if (email.isEmpty() || password.isEmpty() || password.isEmpty()) {
                return Result.failure(Exception("Register failed: Please input the data field"))
            }

            if (universityId == 0L) {
                return Result.failure(Exception("Registration failed: Please select a university"))
            }

            val result = remoteDataSource.register(
                RegisterRequest(
                    username = userName,
                    email = email,
                    roles = roles,
                    password = password,
                    universityId = universityId
                )
            )
            Result.success(result.message)
        } catch (e: Exception) {
            Log.e("error", "$e")
            Result.failure(Exception("Register failed: $e"))
        }
    }

    override suspend fun getQuestions(): Result<List<QuestionResponse>> {
        return try {
            val result = remoteDataSource.getQuestions()
            Result.success(result)
        } catch (e: Exception) {
            Log.e("getQuestions", "Exception: ${e.message}", e)
            Result.failure(Exception("Failed to load questions: $e"))
        }
    }

    override suspend fun getUniversities(): Result<List<UniversityResponse>> {
        return  try {
            val result = remoteDataSource.getUniversities()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load universities: $e"))
        }
    }

    override suspend fun getCourseRecommendation(answers: List<AnsweredQuestionRequest>): Result<List<CourseRecommendationResponse>> {
       return try {
           val result = remoteDataSource.fetchCourseRecommendation(answers)
           Result.success(result)
       } catch (e: Exception) {
           Log.e("getQuestions", "Exception: ${e.message}", e)
           Result.failure(Exception("Failed to get recommendation: $e"))
       }
    }


}