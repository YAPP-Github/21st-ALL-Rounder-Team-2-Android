package com.yapp.gallery

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
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
        lifecycleScope.launchWhenCreated {
            navigate()
        }
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
