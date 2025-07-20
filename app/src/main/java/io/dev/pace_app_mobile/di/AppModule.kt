package io.dev.pace_app_mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.remote.api.ApiService
import io.dev.pace_app_mobile.data.remote.repository.AuthRepositoryImpl
import io.dev.pace_app_mobile.domain.repository.AuthRepository
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiService, tokenManager: TokenManager): AuthRepository =
        AuthRepositoryImpl(api, tokenManager)

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase =
        LoginUseCase(repository)

}