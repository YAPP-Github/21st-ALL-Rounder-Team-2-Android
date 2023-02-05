package com.yapp.gallery.home.navigation

import com.yapp.gallery.navigation.home.HomeNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class HomeNavigatorModule {
    @Binds
    abstract fun bindHomeNavigator(navigatorImpl: HomeNavigatorImpl) : HomeNavigator
}