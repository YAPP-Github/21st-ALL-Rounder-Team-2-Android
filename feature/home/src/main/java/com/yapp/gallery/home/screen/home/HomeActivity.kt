package com.yapp.gallery.home.screen.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.home.navigation.HomeNavHost
import com.yapp.gallery.navigation.profile.ProfileNavigator
import com.yapp.navigation.camera.CameraNavigator
import com.yapp.navigator.saver.SaverNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    @Inject lateinit var cameraNavigator: CameraNavigator
    @Inject lateinit var profileNavigator: ProfileNavigator
    @Inject lateinit var saverNavigator: SaverNavigator
    private lateinit var navController : NavHostController

    private var backKeyPressedTime: Long = 0

    private val imagePicker = registerImagePicker {
        if(it.isNotEmpty()) {
            startActivity(saverNavigator.intentTo(context = this, uris = it.map { image -> image.uri }))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            GalleryTheme {
                HomeNavHost(navHostController = navController, profileNavigator = profileNavigator,
                    cameraNavigator = cameraNavigator, navToImagePicker = {
                        imagePicker.launch(
                            ImagePickerConfig(
                                isMultipleMode = true,
                                maxSize = 5,
                                doneTitle = "완료",
                                limitMessage = "사진은 최대 5장까지 선택 가능해요!"
                            )
                        )
                    }
                )
            }
        }
    }

    override fun onBackPressed() {
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                // 뒤로가기 두 번 누르면 종료
                finishAffinity()
            } else {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "뒤로 가기 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}