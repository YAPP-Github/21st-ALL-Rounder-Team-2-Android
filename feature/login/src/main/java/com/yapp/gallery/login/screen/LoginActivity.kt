package com.yapp.gallery.login.screen

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
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
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.login.BuildConfig
import com.yapp.gallery.navigation.home.HomeNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : ComponentActivity(){
    private val viewModel by viewModels<LoginViewModel>()
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var homeNavigator: HomeNavigator
    @Inject lateinit var sharedPreferences: SharedPreferences

    @Inject lateinit var googleSignInClient: GoogleSignInClient
    @Inject lateinit var kakaoClient: UserApiClient

    private lateinit var naverResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleResultLauncher: ActivityResultLauncher<Intent>

    private var isLoading = mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogin()
        initResultLauncher()
        setContent {
            GalleryTheme {
                LoginScreen(naverLogin =  {naverLogin()}, googleLogin = {googleSignIn()}, kakaoLogin = {kakaoLogin()}, isLoading = isLoading)
            }

            LaunchedEffect(viewModel.tokenState){
                viewModel.tokenState.collect{
                    when (it) {
                        is BaseState.Success -> {
                            firebaseKakaoLogin(it.value)
                        }
                        else -> {}
                    }
                }
            }
            LaunchedEffect(viewModel.loginState){
                viewModel.loginState.collect{
                    when(it){
                        is BaseState.Success -> {
                            // Todo : uid 저장
                            navigateToHome()
                            isLoading.value = false
                        }
                        is BaseState.Loading -> {
                            // Todo : 로딩 화면 만들기
                            isLoading.value = true
                        }
                        is BaseState.Error -> {
                            isLoading.value = false
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initLogin(){
        NaverIdLoginSDK.initialize(this,
            BuildConfig.NAVER_OAUTH_CLIENT_ID,
            BuildConfig.NAVER_OAUTH_CLIENT_SECRET,
            "아르티"
        )
    }

    private fun initResultLauncher(){
        googleResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                firebaseAuthWithGoogle(account.result)
                viewModel.setLoading()
            }
        }
        naverResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when(result.resultCode) {
                RESULT_OK -> {
                    // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가

                }
                RESULT_CANCELED -> {
                    // 실패 or 에러
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(this, "errorCode:$errorCode, errorDesc:$errorDescription", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun googleSignIn(){
        val signInIntent = googleSignInClient.signInIntent
        googleResultLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.e("uid", task.result.user?.uid.toString())
                task.result.user?.apply {
                    getIdToken(false).addOnCompleteListener { t ->
                        sharedPreferences.edit().apply {
                            putString("idToken", t.result.token)
                            putString("loginType", "google")
                        }.apply()

                        uid.let { viewModel.createUser(it)}
                    }
                }
            }else {
                Log.e("google 로그인", task.exception.toString())
            }
        }
    }

    private fun naverLogin(){
        NaverIdLoginSDK.authenticate(this, naverResultLauncher)
    }
   

    private val kakaoCallback : (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            viewModel.postTokenLogin(token.accessToken)
        }
    }

    private fun kakaoLogin(){
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (kakaoClient.isKakaoTalkLoginAvailable(this)) {
            kakaoClient.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    kakaoClient.loginWithKakaoAccount(this, callback = kakaoCallback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    viewModel.postTokenLogin(token.accessToken)
                }
            }
        } else {
            kakaoClient.loginWithKakaoAccount(this, callback = kakaoCallback)
        }

    }

    private fun firebaseKakaoLogin(firebaseToken: String){
        viewModel.setLoading()
        auth.signInWithCustomToken(firebaseToken)
            .addOnCompleteListener { task ->
                Toast.makeText(this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                task.result.user?.apply {
                    getIdToken(false).addOnCompleteListener { t->
                        sharedPreferences.edit().apply {
                            putString("idToken", t.result.token)
                            // Todo : 로그인 타입 설정
                            putString("loginType", "kakao")
                        }.apply()
                        uid.let { viewModel.createUser(it)}
                    }
                }
            }.addOnFailureListener {

            }
    }

    private fun navigateToHome(){
        finishAffinity()
        startActivity(homeNavigator.navigate(this))
    }
}