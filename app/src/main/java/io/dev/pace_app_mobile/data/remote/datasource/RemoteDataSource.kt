package io.dev.pace_app_mobile.data.remote.datasource

import io.dev.pace_app_mobile.data.remote.network.ApiService
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.CustomizationResponse
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.RegisterRequest
import io.dev.pace_app_mobile.domain.model.RegisterResponse
import io.dev.pace_app_mobile.domain.model.StudentAssessmentRequest
import io.dev.pace_app_mobile.domain.model.StudentAssessmentResponse
import io.dev.pace_app_mobile.domain.model.UniversityDomainResponse
import io.dev.pace_app_mobile.domain.model.UniversityLinkResponse
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.model.VerificationCodeRequest
import io.dev.pace_app_mobile.domain.model.VerificationCodeResponse
import io.dev.pace_app_mobile.domain.model.VerifyAccountRequest
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

    suspend fun saveStudentAssessment(studentAssessmentRequest: StudentAssessmentRequest):
            StudentAssessmentResponse {
        val response = api.savedStudentAssessment(studentAssessmentRequest)
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

    suspend fun getStudentAssessment(universityId: Long, email: String): StudentAssessmentResponse {
        val response = api.getStudentAssessment(universityId, email)
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

    suspend fun googleLogin(idToken: String, universityId: Long?): LoginResult {
        val response = api.googleLogin(idToken, universityId)
        if (response.isSuccessful) {
            val loginResult = LoginResult(response.body(), response.code())
            return loginResult
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun facebookLogin(accessToken: String, universityId: Long?): LoginResult {
        val response = api.facebookLogin(accessToken, universityId)
        if (response.isSuccessful) {
            val loginResult = LoginResult(response.body(), response.code())
            return loginResult
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun instagramLogin(accessToken: String, universityId: Long?): LoginResult {
        val response = api.instagramLogin(accessToken, universityId)
        if (response.isSuccessful) {
            val loginResult = LoginResult(response.body(), response.code())
            return loginResult
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun twitterLogin(accessToken: String, universityId: Long?): LoginResult {
        val response = api.twitterLogin(accessToken, universityId)
        if (response.isSuccessful) {
            val loginResult = LoginResult(response.body(), response.code())
            return loginResult
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
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

    suspend fun getAllQuestionsByUniversity(universityId: Long): List<QuestionResponse> {
        val response = api.getAllQuestionsByUniversity(universityId)
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

    suspend fun getUniversityById(universityId: Long): UniversityResponse {
        val response = api.getUniversityById(universityId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                throw Exception("Response body was null for universityId=$universityId")
            }
        } else {
            throw Exception("API error ${response.code()} : ${response.message()}")
        }
    }

    suspend fun isGoogleAccountExist(email: String): Boolean {
        val response = api.isGoogleAccountExist(email)
        if (response.isSuccessful) {
            return response.body() ?: false
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun isFacebookLogin(accessToken: String): Boolean {
        val response = api.isFacebookAccountExist(accessToken)
        if (response.isSuccessful) {
            return response.body() ?: false
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun validateDynamicLink(universityId: Long, token: String): UniversityLinkResponse {
        val response = api.validateToken(universityId, token)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                throw Exception("Response body was null ")
            }
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun getCustomizationTheme(universityId: Long): CustomizationResponse {
        val response = api.getCustomizationTheme(universityId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                throw Exception("Theme not found")
            }
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun getUniversityEmailDomain(universityId: Long): UniversityDomainResponse {
        val response = api.getEmailDomain(universityId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                throw Exception("University domain email not found")
            }
        } else {
            throw Exception(
                "error: " +
                        "${response.code()} : " +
                        "${response.message()}"
            )
        }
    }

    suspend fun sendVerificationCode(verificationCodeRequest: VerificationCodeRequest):
            VerificationCodeResponse {
        val response = api.sendVerificationCode(verificationCodeRequest)
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

    suspend fun verifyAccount(verifyAccountRequest: VerifyAccountRequest): VerificationCodeResponse {
        val response = api.verifyAccount(verifyAccountRequest)
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
}