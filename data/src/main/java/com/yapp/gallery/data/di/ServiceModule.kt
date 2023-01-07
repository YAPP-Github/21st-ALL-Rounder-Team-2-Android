package com.yapp.gallery.data.di

import com.yapp.gallery.data.api.ArtieLoginService
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
    fun provideArtieLoginService(retrofit: Retrofit) : ArtieLoginService {
        return retrofit.create(ArtieLoginService::class.java)
    }
}