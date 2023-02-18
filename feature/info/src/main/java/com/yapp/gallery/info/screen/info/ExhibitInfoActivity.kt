package com.yapp.gallery.info.screen.info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.info.navigation.ExhibitInfoNavHost
<<<<<<< HEAD
import com.yapp.gallery.navigation.home.HomeNavigator
import com.yapp.navigation.camera.CameraNavigator
=======
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExhibitInfoActivity : ComponentActivity() {
    @Inject lateinit var cameraNavigator: CameraNavigator
    @Inject lateinit var homeNavigator: HomeNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
<<<<<<< HEAD
                ExhibitInfoNavHost(
                    cameraNavigator = cameraNavigator, homeNavigator = homeNavigator,
                    context = this
                )
=======
                ExhibitInfoNavHost(context = this)
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
            }
        }
    }
}