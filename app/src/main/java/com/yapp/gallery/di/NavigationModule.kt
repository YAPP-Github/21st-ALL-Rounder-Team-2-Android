package com.yapp.gallery.di

import com.yapp.gallery.home.navigation.HomeNavigatorImpl
import com.yapp.gallery.navigation.home.HomeNavigator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindHomeNavigator(navigatorImpl: HomeNavigatorImpl) : HomeNavigator
}