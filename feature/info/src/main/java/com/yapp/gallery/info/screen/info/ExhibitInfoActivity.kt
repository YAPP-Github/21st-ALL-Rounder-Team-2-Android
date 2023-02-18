package com.yapp.gallery.info.screen.info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.info.navigation.ExhibitInfoNavHost
import com.yapp.gallery.navigation.home.HomeNavigator
import com.yapp.navigation.camera.CameraNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExhibitInfoActivity : ComponentActivity() {
    @Inject
    lateinit var cameraNavigator: CameraNavigator
    @Inject
    lateinit var homeNavigator: HomeNavigator

    private val exhibitId by lazy {
        intent.getLongExtra("exhibitId", 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                ExhibitInfoNavHost(
                    exhibitId = exhibitId,
                    cameraNavigator = cameraNavigator, homeNavigator = homeNavigator,
                    context = this
                )
            }
        }
    }
}
