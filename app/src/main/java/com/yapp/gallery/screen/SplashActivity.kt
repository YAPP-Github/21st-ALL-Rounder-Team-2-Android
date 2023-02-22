package com.yapp.gallery.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yapp.gallery.SplashScreen
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.home.screen.home.HomeActivity
import com.yapp.gallery.login.screen.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val viewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            GalleryTheme {
                SplashScreen()
            }

            LaunchedEffect(viewModel.splashSideEffect){
                viewModel.splashSideEffect.collect{
                    when(it){
                        is SplashEffect.MoveToHome -> moveToHomeLogin(true)
                        is SplashEffect.MoveToLogin -> moveToHomeLogin(false)
                    }
                }
            }
        }
    }

    private fun moveToHomeLogin(isHome: Boolean){
        finishAfterTransition()
        if (isHome) startActivity(Intent(this, HomeActivity::class.java))
        else startActivity(Intent(this, LoginActivity::class.java))
    }
}
