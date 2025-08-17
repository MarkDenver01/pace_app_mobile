package io.dev.pace_app_mobile.di

import androidx.compose.ui.geometry.Rect
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.local.room.dao.LoginDao
import io.dev.pace_app_mobile.data.remote.datasource.RemoteDataSource
import io.dev.pace_app_mobile.data.remote.network.ApiService
import io.dev.pace_app_mobile.data.remote.repository.ApiRepositoryImpl
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.domain.usecase.CourseRecommendationUseCase
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.domain.usecase.QuestionUseCase
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
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
    fun provideCourseRecommendation(repository: ApiRepository): CourseRecommendationUseCase =
        CourseRecommendationUseCase(repository)

}