package io.dev.pace_app_mobile.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.data.remote.network.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.1.29:8080/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(tokenManager: TokenManager): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
                .addHeader("Content-Type", "application/json")

            val path = request.url.encodedPath

            // Skip Authorization only for this specific endpoint
            val shouldSkipAuth = path.startsWith("/user/public/")

            // updated token
            val token = tokenManager.getToken() // TODO changed soon

            if (!shouldSkipAuth && !token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}