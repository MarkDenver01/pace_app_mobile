package io.dev.pace_app_mobile.data.remote.network

import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendationResponse
import io.dev.pace_app_mobile.domain.model.CustomizationResponse
import io.dev.pace_app_mobile.domain.model.LoginRequest
import io.dev.pace_app_mobile.domain.model.LoginResponse
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("user/api/questions/all")
    suspend fun getAllQuestions(): Response<List<QuestionResponse>>

    @GET("user/api/questions/all/{universityId}")
    suspend fun getAllQuestionsByUniversity(@Path("universityId") universityId: Long): Response<List<QuestionResponse>>


    @POST("user/api/course_recommended/top3")
    suspend fun getCourseRecommendation(
        @Body request: List<AnsweredQuestionRequest>
    ): Response<List<CourseRecommendationResponse>>

    @POST("user/api/student_assessment/save")
    suspend fun savedStudentAssessment(
        @Body request: StudentAssessmentRequest
    ): Response<StudentAssessmentResponse>

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


    @GET("user/public/university/select")
    suspend fun getUniversityById(@Query("university_id") universityId: Long?): Response<UniversityResponse>

    @GET("user/public/dynamic_link/validate")
    suspend fun validateToken(
        @Query("universityId") universityId: Long,
        @Query("token") token: String
    ): Response<UniversityLinkResponse>

    @GET("user/public/customization")
    suspend fun getCustomizationTheme(
        @Query("universityId") universityId: Long
    ): Response<CustomizationResponse>

    @GET("user/public/link/email_domain")
    suspend fun getEmailDomain(
        @Query("universityId") universityId: Long
    ): Response<UniversityDomainResponse>

    @POST("user/public/account/send/verification")
    suspend fun sendVerificationCode(@Body verificationCodeRequest: VerificationCodeRequest):
            Response<VerificationCodeResponse>

}