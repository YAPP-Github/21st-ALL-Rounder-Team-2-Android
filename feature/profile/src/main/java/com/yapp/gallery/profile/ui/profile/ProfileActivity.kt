package com.yapp.gallery.profile.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.navigation.login.LoginNavigator
import com.yapp.gallery.profile.navigation.ProfileNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {
    @Inject lateinit var loginNavigator: LoginNavigator

    @Inject lateinit var auth: FirebaseAuth

    @Inject lateinit var googleSignInClient: GoogleSignInClient
    @Inject lateinit var kakaoClient: UserApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GalleryTheme {
                ProfileNavHost(
                    navigateToLogin = { navigateToLogin() }, context = this)
            }
        }
    }


    private fun navigateToLogin(){
        finishAffinity()
        startActivity(loginNavigator.navigate(this))
    }
}