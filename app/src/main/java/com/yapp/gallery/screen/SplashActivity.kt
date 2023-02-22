package com.yapp.gallery.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
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
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val content = findViewById<View>(android.R.id.content)
            content.viewTreeObserver.addOnDrawListener { false }
        }*/
        setSplashScreen()
    }

    private fun setSplashScreen(){
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
