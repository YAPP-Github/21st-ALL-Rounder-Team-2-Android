package com.yapp.gallery.saver

import com.yapp.navigator.saver.SaverNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class SaverModule {

    @Binds
    abstract fun bindsSaverNavigator(navigator: SaverNavigatorImpl): SaverNavigator
}
