package com.yapp.gallery.login.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.navigation.home.HomeNavigator
import com.yapp.gallery.login.ui.LoginContract.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel by viewModels<LoginViewModel>()
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var homeNavigator: HomeNavigator

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    @Inject
    lateinit var kakaoClient: UserApiClient

    private lateinit var naverResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleResultLauncher: ActivityResultLauncher<Intent>

    private var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initResultLauncher()
        setContent {
            GalleryTheme {
                LoginScreen(viewModel = viewModel)
            }

            LaunchedEffect(viewModel.sideEffect){
                viewModel.sideEffect.collectLatest {
                    when(it){
                        is LoginSideEffect.LaunchGoogleLauncher -> {
                            googleSignIn()
                        }
                        is LoginSideEffect.LaunchKakaoLauncher -> {
                            kakaoLogin()
                        }
                        is LoginSideEffect.LaunchNaverLauncher -> {
                            naverLogin()
                        }
                        is LoginSideEffect.NavigateToHome -> {
                            navigateToHome()
                        }
                    }
                }
            }

        }
    }

    private fun initResultLauncher() {
        googleResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    firebaseAuthWithGoogle(account.result)
                } else {
                    viewModel.setEvent(LoginEvent.OnLoginFailure("구글 로그인 실패"))
                }
            }

        naverResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        NaverIdLoginSDK.getAccessToken()?.let {
                            viewModel.setEvent(LoginEvent.OnCreateNaverUser(it))
                        }
                    }
                    else -> {
                        viewModel.setEvent(LoginEvent.OnLoginFailure("네이버 로그인 실패"))
                    }
                }
            }

    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleResultLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                task.result.user?.apply {
                    getIdToken(false).addOnCompleteListener { t ->
                        uid.let { viewModel.setEvent(LoginEvent.OnCreateGoogleUser(it, t.result.token?: "")) }
                    }
                }
            } else {
                viewModel.setEvent(LoginEvent.OnLoginFailure("구글 로그인 실패"))
            }
        }
    }

    private fun naverLogin() {
        NaverIdLoginSDK.authenticate(this, naverResultLauncher)
    }


    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e(error, "카카오계정으로 로그인 실패")
            viewModel.setEvent(LoginEvent.OnLoginFailure("카카오 로그인 실패"))
        } else if (token != null) {
            Timber.i("카카오계정으로 로그인 성공 " + token.accessToken)

            viewModel.setEvent(LoginEvent.OnCreateKakaoUser(token.accessToken))
        }
    }

    private fun kakaoLogin() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (kakaoClient.isKakaoTalkLoginAvailable(this)) {
            kakaoClient.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Timber.e(error, "카카오톡으로 로그인 실패")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    kakaoClient.loginWithKakaoAccount(this, callback = kakaoCallback)
                } else if (token != null) {
                    Timber.i("카카오톡으로 로그인 성공 " + token.accessToken)
                    viewModel.setEvent(LoginEvent.OnCreateKakaoUser(token.accessToken))
                }
            }
        } else {
            kakaoClient.loginWithKakaoAccount(this, callback = kakaoCallback)
        }

    }

    private fun navigateToHome() {
        finishAffinity()
        startActivity(homeNavigator.navigate(this))
    }


    override fun onBackPressed() {
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // 뒤로가기 두 번 누르면 종료
            finishAffinity()
        } else {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 가기 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}