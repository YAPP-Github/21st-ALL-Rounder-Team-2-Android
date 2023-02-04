package com.yapp.gallery.camera.navigation

import com.yapp.navigation.camera.CameraNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class CameraModule {

    @Binds
    abstract fun bindsCameraNavigator(navigator: CameraNavigatorImpl): CameraNavigator
}