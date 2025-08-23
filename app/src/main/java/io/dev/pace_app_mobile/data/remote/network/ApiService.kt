package io.dev.pace_app_mobile.data.remote.network

import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.RegisterRequest
import io.dev.pace_app_mobile.domain.model.RegisterResponse
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("user/public/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("user/public/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("user/api/questions")
    suspend fun getAllQuestions(): Response<List<QuestionResponse>>

    @POST("user/api/course_recommended/top3")
    suspend fun getCourseRecommendation(
        @Body request: List<AnsweredQuestionRequest>
    ): Response<List<CourseRecommendationResponse>>

    @GET("user/public/university/all")
    suspend fun getAllUniversity(): Response<List<UniversityResponse>>

    @FormUrlEncoded
    @POST("user/public/google_login")
    suspend fun googleLogin(
        @Field("idToken") idToken: String,
        @Field("universityId") universityId: Long?
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("user/public/facebook_login")
    suspend fun facebookLogin(
        @Field("accessToken") accessToken: String,
        @Field("universityId") universityId: Long?
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("user/public/instagram_login")
    suspend fun instagramLogin(
        @Field("accessToken") accessToken: String,
        @Field("universityId") universityId: Long?
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("user/public/twitter_login")
    suspend fun twitterLogin(
        @Field("accessToken") accessToken: String,
        @Field("universityId") universityId: Long?
    ): Response<LoginResponse>

    @GET("user/public/check/google_account")
    suspend fun isGoogleAccountExist(@Query("email") email: String): Response<Boolean>

    @GET("user/public/check/facebook_account")
    suspend fun isFacebookAccountExist(@Query("accessToken") accessToken: String): Response<Boolean>
}