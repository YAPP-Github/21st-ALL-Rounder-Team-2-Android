package com.yapp.gallery.profile.screen.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
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

    @Inject lateinit var sharedPreferences : SharedPreferences
    private lateinit var loginType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginType = sharedPreferences.getString("loginType", "").toString()
        setContent {
            GalleryTheme {
                ProfileNavHost(logout = { logout() }, context = this,
                    navigateToLogin = { navigateToLogin() }, loginType = loginType
                )
            }
        }
    }

    private fun logout(){
        when(loginType){
            "kakao" -> {
                kakaoClient.logout {
                    auth.signOut()
                    finishAffinity()
                    startActivity(loginNavigator.navigate(this))
                }
            }
            "naver" -> {
                NaverIdLoginSDK.logout().also {
                    auth.signOut()
                    finishAffinity()
                    startActivity(loginNavigator.navigate(this))
                }
            }
            else -> {
                googleSignInClient.signOut().addOnCompleteListener {
                    auth.signOut()
                    finishAffinity()
                    startActivity(loginNavigator.navigate(this))
                }
            }
        }
    }



    private fun navigateToLogin(){
        finishAffinity()
        startActivity(loginNavigator.navigate(this))
    }
}