package io.dev.pace_app_mobile.domain.repository

import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.presentation.utils.NetworkResult


/**
 * Api repository
 *
 * @constructor Create empty Api repository
 */
interface ApiRepository {
    /**
     * Is existing google account
     *
     * @param email
     * @return
     */
    suspend fun isExistingGoogleAccount(email: String): Boolean

    /**
     * Is existing facebook account.
     *
     * @param accessToken access token
     * @return Boolean
     */
    suspend fun isExistingFacebookAccount(accessToken: String): Boolean

    /**
     * Login
     *
     * @param email
     * @param password
     * @return
     */
    suspend fun login(email: String, password: String): NetworkResult<LoginResponse>

    /**
     * Google login
     *
     * @param idToken
     * @param universityId
     * @return
     */
    suspend fun googleLogin(idToken: String, universityId: Long? = null): NetworkResult<LoginResult>

    /**
     * Create facebook login.
     *
     * @param accessToken access token from facebook
     * @param universityId get university id
     * @return NetworkResult<LoginResult>
     */
    suspend fun facebookLogin(
        accessToken: String,
        universityId: Long? = null
    ): NetworkResult<LoginResult>

    /**
     * Instagram login.
     *
     * @param accessToken
     * @param universityId
     * @return
     */
    suspend fun instagramLogin(
        accessToken: String,
        universityId: Long? = null
    ): NetworkResult<LoginResult>

    /**
     * Twitter login.
     *
     * @param accessToken
     * @param universityId
     * @return
     */
    suspend fun twitterLogin(
        accessToken: String,
        universityId: Long? = null
    ): NetworkResult<LoginResult>

    /**
     * Register
     *
     * @param userName
     * @param email
     * @param roles
     * @param password
     * @param universityId
     * @return
     */
    suspend fun register(
        userName: String,
        email: String,
        roles: Set<String>,
        password: String,
        universityId: Long
    ): Result<String>

    /**
     * Get questions
     *
     * @return
     */
    suspend fun getQuestions(): Result<List<QuestionResponse>>

    /**
     * Get all questions by university
     *
     * @param universityId
     * @return
     */
    suspend fun getAllQuestionsByUniversity(): Result<List<QuestionResponse>>

    /**
     * Get universities
     *
     * @return
     */
    suspend fun getUniversities(): Result<List<UniversityResponse>>

    /**
     * Get course recommendation
     *
     * @param answers
     * @return
     */
    suspend fun getCourseRecommendation(
        answers: List<AnsweredQuestionRequest>
    ): Result<List<CourseRecommendationResponse>>
}