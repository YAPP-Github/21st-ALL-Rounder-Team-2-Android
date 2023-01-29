package com.yapp.gallery.data.di

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.api.login.ArtieKakaoService
import com.yapp.gallery.data.api.login.ArtieNaverService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    @Singleton
    @Provides
    fun provideArtieKakaoLoginService(@NetworkModule.ArtieKakaoRetrofit retrofit: Retrofit) : ArtieKakaoService {
        return retrofit.create(ArtieKakaoService::class.java)
    }

    @Singleton
    @Provides
    fun provideArtieNaverLoginService(@NetworkModule.ArtieNaverRetrofit retrofit: Retrofit) : ArtieNaverService {
        return retrofit.create(ArtieNaverService::class.java)
    }
    @Singleton
    @Provides
    fun provideArtieService(@NetworkModule.ArtieRetrofit retrofit: Retrofit) : ArtieSerivce {
        return retrofit.create(ArtieSerivce::class.java)
    }
}