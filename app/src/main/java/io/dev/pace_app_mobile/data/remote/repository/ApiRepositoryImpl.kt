package io.dev.pace_app_mobile.data.remote.repository

import android.util.Log
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.local.room.entity.LoginEntity
import io.dev.pace_app_mobile.data.remote.datasource.RemoteDataSource
import io.dev.pace_app_mobile.domain.enums.HttpStatus
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.RegisterRequest
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import io.dev.pace_app_mobile.presentation.utils.getHttpStatus
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val loginDao: LoginDao,
    private val tokenManager: TokenManager
) : ApiRepository {

    override suspend fun isExistingGoogleAccount(email: String): Boolean {
        return remoteDataSource.isGoogleAccountExist(email)
    }

    override suspend fun isExistingFacebookAccount(accessToken: String): Boolean {
        return remoteDataSource.isFacebookLogin(accessToken)
    }

    override suspend fun login(email: String, password: String): NetworkResult<LoginResponse> {
        if (email.isEmpty() || password.isEmpty()) {
            return NetworkResult.Error(HttpStatus.BAD_REQUEST, "Email or password cannot be empty")
        }

        return try {
            val loginResponse = remoteDataSource.login(LoginRequest(email, password))

            // gather data
            val loginEntity = LoginEntity(
                userName = loginResponse.username,
                jwtToken = loginResponse.jwtToken,
                role = loginResponse.role
            )

            // save jwt token for API access
            tokenManager.saveToken(loginEntity.jwtToken)
            // insert to db
            loginDao.insertLoginResponse(loginEntity)
            NetworkResult.Success(HttpStatus.OK, loginResponse)
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.INTERNAL_SERVER_ERROR, "${e.message}")
        }
    }

    override suspend fun googleLogin(idToken: String, universityId: Long?): NetworkResult<LoginResult> {
        return try {
            val loginResult = remoteDataSource.googleLogin(idToken, universityId)

            val loginEntity = LoginEntity(
                userName = loginResult.loginResponse?.username ?: "",
                jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                role = loginResult.loginResponse?.role.orEmpty()
            )

            tokenManager.saveToken(loginEntity.jwtToken)
            loginDao.insertLoginResponse(loginEntity)
            NetworkResult.Success(getHttpStatus(loginResult.statusCode), loginResult)
        } catch (e: Exception) {
            NetworkResult.Error(getHttpStatus(401), e.message.toString())
        }
    }

    override suspend fun facebookLogin(
        accessToken: String,
        universityId: Long?
    ): NetworkResult<LoginResult> {
        return try {
            val loginResult = remoteDataSource.facebookLogin(accessToken, universityId)

            val loginEntity = LoginEntity(
                userName = loginResult.loginResponse?.username ?: "",
                jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                role = loginResult.loginResponse?.role.orEmpty()
            )

            tokenManager.saveToken(loginEntity.jwtToken)
            loginDao.insertLoginResponse(loginEntity)

            NetworkResult.Success(getHttpStatus(loginResult.statusCode),
                loginResult)
        } catch (e: Exception) {
            NetworkResult.Error(getHttpStatus(401), e.message.toString())
        }
    }

    override suspend fun instagramLogin(
        accessToken: String,
        universityId: Long?
    ): NetworkResult<LoginResult> {
        return try {
            val loginResult = remoteDataSource.instagramLogin(accessToken, universityId)

            val loginEntity = LoginEntity(
                userName = loginResult.loginResponse?.username ?: "",
                jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                role = loginResult.loginResponse?.role.orEmpty()
            )

            tokenManager.saveToken(loginEntity.jwtToken)
            loginDao.insertLoginResponse(loginEntity)

            NetworkResult.Success(getHttpStatus(loginResult.statusCode),
                loginResult)
        } catch (e: Exception) {
            NetworkResult.Error(getHttpStatus(401), e.message.toString())
        }
    }

    override suspend fun twitterLogin(
        accessToken: String,
        universityId: Long?
    ): NetworkResult<LoginResult> {
        return try {
            val loginResult = remoteDataSource.twitterLogin(accessToken, universityId)

            val loginEntity = LoginEntity(
                userName = loginResult.loginResponse?.username ?: "",
                jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                role = loginResult.loginResponse?.role.orEmpty()
            )

            tokenManager.saveToken(loginEntity.jwtToken)
            loginDao.insertLoginResponse(loginEntity)

            NetworkResult.Success(getHttpStatus(loginResult.statusCode),
                loginResult)
        } catch (e: Exception) {
            NetworkResult.Error(getHttpStatus(401), e.message.toString())
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
        return try {
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