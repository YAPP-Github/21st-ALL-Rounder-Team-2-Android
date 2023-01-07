package com.yapp.gallery.data.di

import com.yapp.gallery.data.api.ArtieService
import com.yapp.gallery.data.api.ArtieTokenService
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
    fun provideArtieLoginService(@NetworkModule.ArtieTokenRetrofit retrofit: Retrofit) : ArtieTokenService {
        return retrofit.create(ArtieTokenService::class.java)
    }

    @Singleton
    @Provides
    fun provideArtieService(@NetworkModule.ArtieRetrofit retrofit: Retrofit) : ArtieService {
        return retrofit.create(ArtieService::class.java)
    }
}