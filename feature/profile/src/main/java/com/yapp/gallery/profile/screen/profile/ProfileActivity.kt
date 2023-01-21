package com.yapp.gallery.profile.screen.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
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
                    viewModel = viewModel
                )
            }

            LaunchedEffect(viewModel.loginType){
                viewModel.loginType.collectLatest {
                    if (it.isNotEmpty()) logout(it)
                }
            }
        }
    }

    private fun navigateToManage(){
        startActivity(Intent(this, CategoryManageActivity::class.java))
    }

    private fun logout(type: String){
        when(type){
            "kakao" -> {
                kakaoClient.logout {
                    auth.signOut()
                    finishAffinity()
                    startActivity(loginNavigator.navigate(this))
                }
            }
            // Todo : 네이버 로그아웃 추가
            else -> {
                googleSignInClient.signOut().addOnCompleteListener {
                    auth.signOut()
                    finishAffinity()
                    startActivity(loginNavigator.navigate(this))
                }
            }
        }
    }
}