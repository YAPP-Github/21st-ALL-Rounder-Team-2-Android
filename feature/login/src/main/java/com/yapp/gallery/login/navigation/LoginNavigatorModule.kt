package com.yapp.gallery.login.navigation

import com.yapp.gallery.navigation.login.LoginNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class LoginNavigatorModule {
    @Binds
    abstract fun bindLoginNavigator(loginNavigatorImpl: LoginNavigatorImpl): LoginNavigator
}