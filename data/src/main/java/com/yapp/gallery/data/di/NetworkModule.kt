package com.yapp.gallery.data.di

import com.yapp.gallery.data.BuildConfig
import com.yapp.gallery.data.utils.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.math.log

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    // Firebase Custom 토큰 서비스 담당 레트로핏
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ArtieTokenRetrofit

    // 아르티 서비스 API 담당 레트로핏
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ArtieRetrofit

    // 아르티 서비스 클라이언트
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ArtieClient

    @Singleton
    @Provides
    @ArtieTokenRetrofit
    fun providesTokenRetrofit(gsonConverterFactory: GsonConverterFactory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TOKEN_SERVER_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    @ArtieRetrofit
    fun providesLoginRetrofit(gsonConverterFactory: GsonConverterFactory, @ArtieClient client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(authInterceptor: AuthInterceptor) : Interceptor = authInterceptor

    @Provides
    @Singleton
    @ArtieClient
    fun provideArtieHttpClient(loggingInterceptor: HttpLoggingInterceptor, interceptor: Interceptor) : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory() : GsonConverterFactory{
        return GsonConverterFactory.create()
    }

}