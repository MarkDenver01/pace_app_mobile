package io.dev.pace_app_mobile.di

import android.content.Context
import androidx.compose.ui.geometry.Rect
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.dev.pace_app_mobile.data.local.datastore.DynamicLinkDataStore
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.remote.datasource.RemoteDataSource
import io.dev.pace_app_mobile.data.remote.network.ApiService
import io.dev.pace_app_mobile.data.remote.repository.ApiRepositoryImpl
import io.dev.pace_app_mobile.data.remote.repository.DynamicLinkRepositoryImpl
import io.dev.pace_app_mobile.domain.model.CustomizationResponse
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.domain.repository.DynamicLinkRepository
import io.dev.pace_app_mobile.domain.usecase.AllQuestionsByUniversityUseCase
import io.dev.pace_app_mobile.domain.usecase.CourseRecommendationUseCase
import io.dev.pace_app_mobile.domain.usecase.CustomizationUseCase
import io.dev.pace_app_mobile.domain.usecase.DynamicLinkValidationUseCase
import io.dev.pace_app_mobile.domain.usecase.FacebookAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.FacebookLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.GetDynamicLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.GoogleAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.GoogleLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.InstagramLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.domain.usecase.QuestionUseCase
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
import io.dev.pace_app_mobile.domain.usecase.SaveDynamicLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.StudentAssessmentUseCase
import io.dev.pace_app_mobile.domain.usecase.TwitterLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityUseCase
import io.dev.pace_app_mobile.domain.usecase.UpdateVerificationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRemoteDataSource(apiService: ApiService): RemoteDataSource =
        RemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideAuthRepository(
        remoteDataSource: RemoteDataSource,
        loginDao: LoginDao,
        tokenManager: TokenManager
    ): ApiRepository =
        ApiRepositoryImpl(remoteDataSource, loginDao, tokenManager)

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: ApiRepository): LoginUseCase =
        LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: ApiRepository): RegisterUseCase =
        RegisterUseCase(repository)

    @Provides
    @Singleton
    fun provideQuestionUseCase(repository: ApiRepository): QuestionUseCase =
        QuestionUseCase(repository)

    @Provides
    @Singleton
    fun provideAllQuestionsByUniversityUseCase(repository: ApiRepository): AllQuestionsByUniversityUseCase =
        AllQuestionsByUniversityUseCase(repository)

    @Provides
    @Singleton
    fun provideCourseRecommendation(repository: ApiRepository): CourseRecommendationUseCase =
        CourseRecommendationUseCase(repository)

    @Provides
    @Singleton
    fun provideUniversityUseCase(repository: ApiRepository): UniversityUseCase =
        UniversityUseCase(repository)

    @Provides
    @Singleton
    fun provideGoogleLoginUseCase(repository: ApiRepository): GoogleLoginUseCase =
        GoogleLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideFacebookLoginUseCase(repository: ApiRepository): FacebookLoginUseCase =
        FacebookLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideInstagramLoginUseCase(repository: ApiRepository): InstagramLoginUseCase =
        InstagramLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideTwitterLoginUseCase(repository: ApiRepository): TwitterLoginUseCase =
        TwitterLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideGoogleAccountUseCase(repository: ApiRepository): GoogleAccountUseCase =
        GoogleAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideFacebookAccountUseCase(repository: ApiRepository): FacebookAccountUseCase =
        FacebookAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideDynamicLinkValidationUseCase(repository: ApiRepository): DynamicLinkValidationUseCase =
        DynamicLinkValidationUseCase(repository)

    @Provides
    @Singleton
    fun provideCustomizationUseCase(repository: ApiRepository): CustomizationUseCase =
        CustomizationUseCase(repository)

    @Provides
    @Singleton
    fun provideStudentAssessmentUseCase(repository: ApiRepository): StudentAssessmentUseCase =
        StudentAssessmentUseCase(repository)

    @Provides
    @Singleton
    fun provideDynamicLinkDataStore(@ApplicationContext context: Context) =
        DynamicLinkDataStore(context)

    @Provides
    @Singleton
    fun provideDynamicLinkRepository(
        dataStore: DynamicLinkDataStore
    ): DynamicLinkRepository = DynamicLinkRepositoryImpl(dataStore)

    @Provides
    fun provideSaveDynamicLinkUseCase(repo: DynamicLinkRepository) = SaveDynamicLinkUseCase(repo)

    @Provides
    fun provideGetDynamicLinkUseCase(repo: DynamicLinkRepository) = GetDynamicLinkUseCase(repo)

    @Provides
    fun provideUpdateVerificationUseCase(repo: DynamicLinkRepository) =
        UpdateVerificationUseCase(repo)
}