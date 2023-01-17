package com.yapp.gallery.profile.navigation

import com.yapp.gallery.navigation.profile.ProfileNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileNavigatorModule {
    @Binds
    @Singleton
    abstract fun bindProfileNavigator(navigatorImpl: ProfileNavigatorImpl) : ProfileNavigator
}