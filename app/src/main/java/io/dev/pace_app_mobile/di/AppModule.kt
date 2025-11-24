package io.dev.pace_app_mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.dev.pace_app_mobile.data.local.datastore.LocalDataStore
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.remote.datasource.RemoteDataSource
import io.dev.pace_app_mobile.data.remote.network.ApiService
import io.dev.pace_app_mobile.data.remote.repository.ApiRepositoryImpl
import io.dev.pace_app_mobile.data.local.repository.LocalDataStoreRepositoryImpl
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import io.dev.pace_app_mobile.domain.usecase.AllQuestionsByUniversityUseCase
import io.dev.pace_app_mobile.domain.usecase.CourseRecommendationUseCase
import io.dev.pace_app_mobile.domain.usecase.CustomizationUseCase
import io.dev.pace_app_mobile.domain.usecase.DeleteStudentAssessmentUseCase
import io.dev.pace_app_mobile.domain.usecase.DynamicLinkValidationUseCase
import io.dev.pace_app_mobile.domain.usecase.FacebookAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.FacebookLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.GetDynamicLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.GetVerifiedAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.GoogleAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.GoogleLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.GetStudentAssessmentUseCase
import io.dev.pace_app_mobile.domain.usecase.InstagramLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.domain.usecase.QuestionUseCase
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
import io.dev.pace_app_mobile.domain.usecase.SaveDynamicLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.SaveUniversityIdUseCase
import io.dev.pace_app_mobile.domain.usecase.SavedVerifiedAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.StudentAssessmentUseCase
import io.dev.pace_app_mobile.domain.usecase.TwitterLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityDomainEmailUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityUseCase
import io.dev.pace_app_mobile.domain.usecase.UpdateStudentPasswordUseCase
import io.dev.pace_app_mobile.domain.usecase.UpdateUserNameUseCase
import io.dev.pace_app_mobile.domain.usecase.UpdateVerificationUseCase
import io.dev.pace_app_mobile.domain.usecase.VerificationCodeUseCase
import io.dev.pace_app_mobile.domain.usecase.VerifyAccountUseCase
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
    fun provideGetStudentAssessmentUseCase(repository: ApiRepository): GetStudentAssessmentUseCase =
        GetStudentAssessmentUseCase(repository)

    @Provides
    @Singleton
    fun provideDynamicLinkDataStore(@ApplicationContext context: Context) =
        LocalDataStore(context)

    @Provides
    @Singleton
    fun provideLocalDataStoreRepository(
        dataStore: LocalDataStore
    ): LocalDataStoreRepository = LocalDataStoreRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideSaveDynamicLinkUseCase(repository: LocalDataStoreRepository) =
        SaveDynamicLinkUseCase(repository)

    @Provides
    @Singleton
    fun provideGetDynamicLinkUseCase(repository: LocalDataStoreRepository) =
        GetDynamicLinkUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateVerificationUseCase(repository: LocalDataStoreRepository) =
        UpdateVerificationUseCase(repository)

    @Provides
    @Singleton
    fun provideGetVerifiedAccountUseCase(repository: LocalDataStoreRepository) =
        GetVerifiedAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveVerifiedAccountUseCase(repository: LocalDataStoreRepository) =
        SavedVerifiedAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideUniversityDomainEmailUseCase(repository: ApiRepository) =
        UniversityDomainEmailUseCase(repository)

    @Provides
    @Singleton
    fun provideVerificationCodeUseCase(repository: ApiRepository) =
        VerificationCodeUseCase(repository)

    @Provides
    @Singleton
    fun provideVerifyAccountUseCase(repository: ApiRepository) =
        VerifyAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteStudentAssessment(repository: ApiRepository) =
        DeleteStudentAssessmentUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateUserNameUseCase(repository: ApiRepository) =
        UpdateUserNameUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateStudentPasswordUseCase(repository: ApiRepository) =
        UpdateStudentPasswordUseCase(repository)


    @Provides
    @Singleton
    fun provideUniversityIdUseCase(apiRepository: ApiRepository) =
        SaveUniversityIdUseCase(apiRepository)
}