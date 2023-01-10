package com.yapp.gallery.home.navigation

import com.yapp.gallery.navigation.home.HomeNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeNavigatorModule {
    @Binds
    @Singleton
    abstract fun bindHomeNavigator(navigatorImpl: HomeNavigatorImpl) : HomeNavigator
}