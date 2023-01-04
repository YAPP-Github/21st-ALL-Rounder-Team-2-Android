package com.yapp.gallery.login

import android.R.attr.data
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.navigation.home.HomeNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : ComponentActivity(){
    private val viewModel by viewModels<LoginViewModel>()
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var homeNavigator: HomeNavigator

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    private val kakaoClient by lazy {
        UserApiClient.instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initOneTap()
        initGoogleLogin()
        initResultLauncher()
        setContent {
            GalleryTheme {
                LoginScreen(googleLogin = {googleSignIn()}, kakaoLogin = {kakaoLogin()})
            }
        }
    }

//    private fun initOneTap(){
//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
//            .setAutoSelectEnabled(true)
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(BuildConfig.FIREBASE_WEB_CLIENT_ID)
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(false)
//                    .build())
//            .build()
//    }

    private fun initGoogleLogin(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private fun initResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                firebaseAuthWithGoogle(account.result)
            }
        }

    }
    private fun googleSignIn(){
        val signInIntent = mGoogleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                navigateToHome()
                Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_SHORT).show()
            }else {
                Log.e("google 로그인", task.exception.toString())
            }
        }
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

    private fun firebaseKakaoLogin(){
        kakaoClient.me { user, error ->
            val name = user?.kakaoAccount?.profile?.nickname
            val kakaoId = user?.id
            Log.e("kakaoName", name.toString())

            // Todo : 임시
            auth.signInWithEmailAndPassword("kakao$kakaoId@test.com", "123456")
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        Log.e("kakao", "kakao Login Success")
                    else{
                        Log.e("kakao", "kakao Login Fail : ${it.exception}")
                        auth.createUserWithEmailAndPassword("kakao$kakaoId@test.com", "123456")
                    }
                    navigateToHome()
                }
        }
        // Todo : 커스텀 토큰으로 로그인
        // auth.signInWithCustomToken()
    }

    private fun navigateToHome(){
        finishAffinity()
        startActivity(homeNavigator.navigate(this))
    }
}