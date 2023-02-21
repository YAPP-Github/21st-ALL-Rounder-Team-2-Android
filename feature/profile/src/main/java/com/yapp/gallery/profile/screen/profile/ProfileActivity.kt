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

    private val loginType by lazy {
        sharedPreferences.getString("loginType", "").toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                ProfileNavHost(logout = { logout() }, signOut = { signOut() }, context = this,
                    navigateToLogin = { navigateToLogin() }
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

    private fun signOut(){
        Log.e("loginType", loginType)
        when(loginType){
            "kakao" -> {
                kakaoClient.unlink {
                    auth.currentUser?.delete()
                }
            }
            "naver" -> {
                NidOAuthLogin().callDeleteTokenApi(this, object : OAuthLoginCallback{
                    override fun onError(errorCode: Int, message: String) {
                        onFailure(errorCode, message)
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        Log.d("네이버 회원탈퇴", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                        Log.d("네이버 회원탈퇴", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                    }

                    override fun onSuccess() {
                        auth.currentUser?.delete()?.addOnCompleteListener {
                            Log.e("firebase 삭제", it.isSuccessful.toString())
                        }
                    }

                })
            }
            else -> {
                googleSignInClient.revokeAccess().addOnCompleteListener {
                    auth.currentUser?.delete()
                }
            }
        }
    }

    private fun navigateToLogin(){
        finishAffinity()
        startActivity(loginNavigator.navigate(this))
    }
}