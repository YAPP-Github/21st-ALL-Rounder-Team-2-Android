package com.yapp.gallery.info.navigation

import com.yapp.gallery.navigation.info.ExhibitInfoNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ExhibitInfoNavigatorModule {
    @Binds
    abstract fun bindExhibitInfoNavigator(navigatorImpl: ExhibitInfoNavigatorImpl) : ExhibitInfoNavigator
}