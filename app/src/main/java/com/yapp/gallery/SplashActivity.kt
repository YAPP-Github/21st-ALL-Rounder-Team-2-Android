package com.yapp.gallery

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.home.screen.home.HomeActivity
import com.yapp.gallery.login.screen.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    @Inject lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{ false }
        navigate()
    }

    private fun navigate(){
        finishAfterTransition()
        auth.currentUser?.let {
            startActivity(Intent(this, HomeActivity::class.java))
        } ?: run{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
