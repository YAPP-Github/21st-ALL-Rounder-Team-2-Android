package com.yapp.gallery.home.screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.home.navigation.HomeNavHost
import com.yapp.gallery.navigation.home.HomeNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private lateinit var navController : NavHostController
    private var backKeyPressedTime: Long = 0

    //    @Inject lateinit var homeNavigator: HomeNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            GalleryTheme {
                HomeNavHost(navHostController = navController)
            }
            //HomeScreen()
        }
    }

    override fun onBackPressed() {
        if (navController.previousBackStackEntry != null){
            navController.popBackStack()
        } else {
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                // 뒤로가기 두 번 누르면 종료
                finishAffinity()
            } else{
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "뒤로 가기 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}