package com.yapp.gallery.profile.screen.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.navigation.login.LoginNavigator
import com.yapp.gallery.profile.screen.category.CategoryManageActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {
    private val viewModel by viewModels<ProfileViewModel>()

    @Inject lateinit var loginNavigator: LoginNavigator

    @Inject lateinit var auth: FirebaseAuth

    @Inject lateinit var googleSignInClient: GoogleSignInClient
    @Inject lateinit var kakaoClient: UserApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                ProfileScreen(popBackStack = { finish() }, navigateToManage = { navigateToManage() },
                    logout = { logout() }, withdrawal = { withdrawal() }, viewModel = viewModel
                )
            }
        }
    }

    private fun navigateToManage(){
        startActivity(Intent(this, CategoryManageActivity::class.java))
    }

    private fun logout(){
        viewModel.removeInfo()
        when(viewModel.loginType){
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

    private fun withdrawal(){
        Log.e("loginType", viewModel.loginType)
        // Todo : 서버에서 회원 탈퇴하는것도 만들어야함
        when(viewModel.loginType){
            "kakao" -> {
                kakaoClient.unlink {
                    viewModel.removeInfo()
                    auth.currentUser?.delete()?.addOnCompleteListener {
                        finishAffinity()
                        startActivity(loginNavigator.navigate(this))
                    }
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
                        viewModel.removeInfo()
                        auth.currentUser?.delete()?.addOnCompleteListener {
                            Log.e("firebase 삭제", it.isSuccessful.toString())
                            finishAffinity()
                            startActivity(loginNavigator.navigate(this@ProfileActivity))
                        }
                    }

                })
            }
            else -> {
                googleSignInClient.revokeAccess().addOnCompleteListener {
                    viewModel.removeInfo()
                    auth.currentUser?.delete()?.addOnCompleteListener {
                        finishAffinity()
                        startActivity(loginNavigator.navigate(this))
                    }
                }
            }
        }
    }
}