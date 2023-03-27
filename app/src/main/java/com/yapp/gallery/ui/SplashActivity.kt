package com.yapp.gallery.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.home.ui.home.HomeActivity
import com.yapp.gallery.login.ui.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    @Inject lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        //
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val content: View = findViewById(android.R.id.content)
//            content.viewTreeObserver.addOnPreDrawListener { false }
//        }

        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(1000)
            auth.currentUser?.let {
                moveToHomeLogin(true)
            } ?: run {
                moveToHomeLogin(false)
            }
        }
    }

    private fun moveToHomeLogin(isHome: Boolean){
        finishAfterTransition()
        if (isHome) startActivity(Intent(this, HomeActivity::class.java))
        else startActivity(Intent(this, LoginActivity::class.java))
    }
}
