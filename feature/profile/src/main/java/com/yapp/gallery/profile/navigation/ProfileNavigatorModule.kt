package com.yapp.gallery.profile.navigation

import com.yapp.gallery.navigation.profile.ProfileNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class ProfileNavigatorModule {
    @Binds
    abstract fun bindProfileNavigator(navigatorImpl: ProfileNavigatorImpl) : ProfileNavigator
}