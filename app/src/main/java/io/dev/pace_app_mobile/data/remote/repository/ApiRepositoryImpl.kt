package io.dev.pace_app_mobile.data.remote.repository

import android.util.Log
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.local.room.entity.LoginEntity
import io.dev.pace_app_mobile.data.remote.datasource.RemoteDataSource
import io.dev.pace_app_mobile.domain.enums.HttpStatus
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.CustomizationResponse
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.RegisterRequest
import io.dev.pace_app_mobile.domain.model.StudentAssessmentRequest
import io.dev.pace_app_mobile.domain.model.StudentAssessmentResponse
import io.dev.pace_app_mobile.domain.model.UniversityDomainResponse
import io.dev.pace_app_mobile.domain.model.UniversityLinkResponse
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.model.VerificationCodeRequest
import io.dev.pace_app_mobile.domain.model.VerificationCodeResponse
import io.dev.pace_app_mobile.domain.model.VerifyAccountRequest
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import io.dev.pace_app_mobile.presentation.utils.getHttpStatus
import timber.log.Timber
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

            if (loginDao.isAccountExist(email)) {
                // save jwt token for API access
                tokenManager.saveToken(loginResponse.jwtToken ?: "")
                tokenManager.saveUniversityId(loginResponse.studentResponse?.universityId ?: 0L)
                loginDao.updateLoginByEmail(
                    email = email,
                    userName = loginResponse.username ?: "",
                    jwtToken = loginResponse.jwtToken ?: "",
                    role = loginResponse.role ?: ""
                )


            } else {
                // gather data
                val loginEntity = LoginEntity(
                    userName = loginResponse.username ?: "",
                    jwtToken = loginResponse.jwtToken ?: "",
                    role = loginResponse.role ?: "",
                    universityId = loginResponse.studentResponse?.universityId ?: 0L,
                    email = email
                )

                // save jwt token for API access
                tokenManager.saveToken(loginEntity.jwtToken)
                tokenManager.saveUniversityId(loginEntity.universityId!!)
                // insert to db
                loginDao.insertLoginResponse(loginEntity)
            }

            NetworkResult.Success(HttpStatus.OK, loginResponse)
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.INTERNAL_SERVER_ERROR, "${e.message}")
        }
    }

    override suspend fun googleLogin(
        idToken: String,
        universityId: Long?
    ): NetworkResult<LoginResult> {
        return try {
            val loginResult = remoteDataSource.googleLogin(idToken, universityId)

            val checkEmail = loginResult.loginResponse?.studentResponse?.email ?: ""
            if (loginDao.isAccountExist(checkEmail)) {
                // save jwt token for API access
                tokenManager.saveToken(loginResult.loginResponse?.jwtToken ?: "")
                tokenManager.saveUniversityId(
                    loginResult.loginResponse?.studentResponse?.universityId
                        ?: 1L
                )
                loginDao.updateLoginByEmail(
                    email = loginResult.loginResponse?.studentResponse?.email ?: "",
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role ?: ""
                )

                val universityId = loginDao.getUniversityId(checkEmail)
                Timber.d("check university id from googl login: $universityId")
            } else {
                // gather data
                val loginEntity = LoginEntity(
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role.orEmpty(),
                    universityId = universityId,
                    email = loginResult.loginResponse?.studentResponse?.email ?: ""
                )
                tokenManager.saveToken(loginEntity.jwtToken)
                tokenManager.saveUniversityId(loginEntity.universityId!!)
                loginDao.insertLoginResponse(loginEntity)
                Timber.d("check university id from google login: ${loginEntity.universityId}")
            }
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

            val checkEmail = loginResult.loginResponse?.studentResponse?.email ?: ""
            if (loginDao.isAccountExist(checkEmail)) {
                // save jwt token for API access
                tokenManager.saveToken(loginResult.loginResponse?.jwtToken ?: "")
                tokenManager.saveUniversityId(
                    loginResult.loginResponse?.studentResponse?.universityId
                        ?: 1L
                )
                loginDao.updateLoginByEmail(
                    email = loginResult.loginResponse?.studentResponse?.email ?: "",
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role ?: ""
                )

                val universityId = loginDao.getUniversityId(checkEmail)
                Timber.d("xxxxxxx n: $universityId")
            } else {
                // gather data
                val loginEntity = LoginEntity(
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role.orEmpty(),
                    universityId = universityId,
                    email = loginResult.loginResponse?.studentResponse?.email ?: ""
                )
                tokenManager.saveToken(loginEntity.jwtToken)
                tokenManager.saveUniversityId(loginEntity.universityId!!)
                loginDao.insertLoginResponse(loginEntity)
                Timber.d("check university id from facebook login: ${loginEntity.universityId}")
            }
            NetworkResult.Success(
                getHttpStatus(loginResult.statusCode),
                loginResult
            )
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

            val checkEmail = loginResult.loginResponse?.studentResponse?.email ?: ""
            if (loginDao.isAccountExist(checkEmail)) {
                // save jwt token for API access
                tokenManager.saveToken(loginResult.loginResponse?.jwtToken ?: "")
                tokenManager.saveUniversityId(
                    loginResult.loginResponse?.studentResponse?.universityId
                        ?: 1L
                )
                loginDao.updateLoginByEmail(
                    email = loginResult.loginResponse?.studentResponse?.email ?: "",
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role ?: ""
                )

                val universityId = loginDao.getUniversityId(
                    checkEmail
                )
                Timber.d("check university id from insta login: $universityId")
            } else {
                // gather data
                val loginEntity = LoginEntity(
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role.orEmpty(),
                    universityId = universityId,
                    email = loginResult.loginResponse?.studentResponse?.email ?: ""
                )
                tokenManager.saveToken(loginEntity.jwtToken)
                tokenManager.saveUniversityId(loginEntity.universityId!!)
                loginDao.insertLoginResponse(loginEntity)
                Timber.d("check university id from instagram login: ${loginEntity.universityId}")
            }

            NetworkResult.Success(
                getHttpStatus(loginResult.statusCode),
                loginResult
            )

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

            val checkEmail = loginResult.loginResponse?.studentResponse?.email ?: ""
            if (loginDao.isAccountExist(checkEmail)) {
                // save jwt token for API access
                tokenManager.saveToken(loginResult.loginResponse?.jwtToken ?: "")
                tokenManager.saveUniversityId(
                    loginResult.loginResponse?.studentResponse?.universityId
                        ?: 1L
                )
                loginDao.updateLoginByEmail(
                    email = loginResult.loginResponse?.studentResponse?.email ?: "",
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role ?: ""
                )

                val universityId = loginDao.getUniversityId(
                    checkEmail
                )
                Timber.d("check university id from twit login: $universityId")
            } else {
                // gather data
                val loginEntity = LoginEntity(
                    userName = loginResult.loginResponse?.username ?: "",
                    jwtToken = loginResult.loginResponse?.jwtToken ?: "",
                    role = loginResult.loginResponse?.role.orEmpty(),
                    universityId = universityId,
                    email = loginResult.loginResponse?.studentResponse?.email ?: ""
                )
                tokenManager.saveToken(loginEntity.jwtToken)
                tokenManager.saveUniversityId(loginEntity.universityId!!)
                loginDao.insertLoginResponse(loginEntity)
                Timber.d("check university id from twitter login: ${loginEntity.universityId}")
            }

            NetworkResult.Success(
                getHttpStatus(loginResult.statusCode),
                loginResult
            )
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

            // Apply Fisher–Yates Shuffle (via Kotlin's built-in shuffled())
            val randomizedResult = result.shuffled()

            Result.success(randomizedResult)
        } catch (e: Exception) {
            Log.e("getQuestions", "Exception: ${e.message}", e)
            Result.failure(Exception("Failed to load questions: $e"))
        }
    }

    override suspend fun getAllQuestionsByUniversity(): Result<List<QuestionResponse>> {
        return try {
            val result = remoteDataSource.getAllQuestionsByUniversity(
                tokenManager.getUniversityId()!!
            )
            Result.success(result)
        } catch (e: Exception) {
            Timber.e("getAllQuestionsByUniversity error: ${e.message}")
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

    override suspend fun getUniversityById(universityId: Long): Result<UniversityResponse> {
        return try {
            val result = remoteDataSource.getUniversityById(universityId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get university: $e"))
        }
    }

    override suspend fun getCourseRecommendation(answers: List<AnsweredQuestionRequest>): Result<List<CourseRecommendationResponse>> {
        return try {
            val result = remoteDataSource.fetchCourseRecommendation(answers)
            Result.success(result)
        } catch (e: Exception) {
            Timber.e("getQuestions - exception: ${e.message}")
            Result.failure(Exception("Failed to get recommendation: $e"))
        }
    }

    override suspend fun saveStudentAssessment(studentAssessmentRequest: StudentAssessmentRequest): NetworkResult<StudentAssessmentResponse> {
        return try {
            val result = remoteDataSource.saveStudentAssessment(studentAssessmentRequest)
            NetworkResult.Success(HttpStatus.OK, result)
        } catch (e: Exception) {
            Timber.e("getQuestions - exception: ${e.message}")
            NetworkResult.Error(HttpStatus.BAD_REQUEST, e.message.toString())
        }
    }

    override suspend fun getStudentAssessment(
        universityId: Long,
        email: String
    ): NetworkResult<StudentAssessmentResponse> {
        return try {
            val studentAssessmentResponse = remoteDataSource.getStudentAssessment(
                universityId,
                email)
            NetworkResult.Success(HttpStatus.OK, studentAssessmentResponse)
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.BAD_REQUEST, e.message.toString())
        }
    }

    override suspend fun validateDynamicLink(
        universityId: Long,
        token: String
    ): NetworkResult<UniversityLinkResponse> {
        return try {
            val universityLinkResponse = remoteDataSource.validateDynamicLink(universityId, token)
            NetworkResult.Success(HttpStatus.OK, universityLinkResponse)
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.BAD_REQUEST, e.message.toString())
        }
    }

    override suspend fun getCustomizationTheme(universityId: Long): NetworkResult<CustomizationResponse> {
        return try {
            val result = remoteDataSource.getCustomizationTheme(universityId)
            NetworkResult.Success(HttpStatus.OK, result)
        } catch (e: Exception) {
            Timber.e("getCustomizationTheme - exception: ${e.message}")
            NetworkResult.Error(HttpStatus.BAD_REQUEST, e.message.toString())
        }
    }

    override suspend fun getUniversityDomainEmail(universityId: Long): NetworkResult<UniversityDomainResponse> {
        return try {
            val result = remoteDataSource.getUniversityEmailDomain(universityId)
            NetworkResult.Success(HttpStatus.OK, result)
        } catch (e: Exception) {
            Timber.e("getUniversityDomainEmail error: ${e.message}")
            NetworkResult.Error(HttpStatus.NOT_FOUND, "University domain email not found")
        }
    }

    override suspend fun sendVerificationCode(verificationCodeRequest: VerificationCodeRequest): NetworkResult<VerificationCodeResponse> {
        return try {
            val result = remoteDataSource.sendVerificationCode(verificationCodeRequest)
            NetworkResult.Success(
                HttpStatus.OK,
                result
            )
        } catch (e: Exception) {
            Timber.e("exception: ${e.message}")
            NetworkResult.Error(HttpStatus.BAD_REQUEST, "exception: ${e.message}")
        }
    }

    override suspend fun verifyAccount(
        email: String,
        verificationCode: Int
    ): NetworkResult<VerificationCodeResponse> {
        return try {
            val verifyAccountRequest = VerifyAccountRequest(email, verificationCode)
            val result = remoteDataSource.verifyAccount(verifyAccountRequest
            )
            NetworkResult.Success(
                HttpStatus.OK,
                result
            )
        } catch (e: Exception) {
            Timber.e("exception: ${e.message}")
            NetworkResult.Error(HttpStatus.BAD_REQUEST, "exception: ${e.message}")
        }
    }

}