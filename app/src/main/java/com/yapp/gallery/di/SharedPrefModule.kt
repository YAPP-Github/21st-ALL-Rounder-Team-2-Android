package com.yapp.gallery.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SharedPrefModule {
    @Provides
    @Singleton
    fun provideSharedPrefManager(@ApplicationContext context: Context) : SharedPreferences{
        return context.getSharedPreferences("artieSpf", Context.MODE_PRIVATE)
    }
}