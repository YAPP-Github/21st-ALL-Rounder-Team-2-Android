package com.yapp.gallery.di

import android.content.Context
import android.content.SharedPreferences
import com.yapp.gallery.home.navigation.HomeNavigatorImpl
import com.yapp.gallery.navigation.home.HomeNavigator
import dagger.Binds
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