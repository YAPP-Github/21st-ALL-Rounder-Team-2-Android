package com.yapp.gallery.camera.navigation

import android.content.Context
import android.content.Intent
import com.yapp.gallery.camera.CameraActivity
import com.yapp.navigation.camera.CameraNavigator
import javax.inject.Inject

class CameraNavigatorImpl @Inject constructor(): CameraNavigator {

    override fun navigate(context: Context): Intent {
        return Intent(context, CameraActivity::class.java)
    }

}
