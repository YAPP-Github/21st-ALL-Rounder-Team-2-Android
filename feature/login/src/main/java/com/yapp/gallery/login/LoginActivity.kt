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
import kotlinx.coroutines.flow.collect
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
        initGoogleLogin()
        initResultLauncher()
        setContent {
            GalleryTheme {
                LoginScreen(googleLogin = {googleSignIn()}, kakaoLogin = {kakaoLogin()})
            }
            LaunchedEffect(viewModel.loginState){
                viewModel.loginState.collect{
                    when (it) {
                        is LoginState.Success -> {
                            firebaseKakaoLogin(it.token)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

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
                Toast.makeText(this, "?????? ????????? ??????", Toast.LENGTH_SHORT).show()
            }else {
                Log.e("google ?????????", task.exception.toString())
            }
        }
    }
   

    private val kakaoCallback : (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "????????????????????? ????????? ??????", error)
        } else if (token != null) {
            Log.i(TAG, "????????????????????? ????????? ?????? ${token.accessToken}")
            viewModel.postTokenLogin(token.accessToken)
        }
    }

    private fun kakaoLogin(){
        // ??????????????? ???????????? ????????? ?????????????????? ?????????, ????????? ????????????????????? ?????????
        if (kakaoClient.isKakaoTalkLoginAvailable(this)) {
            kakaoClient.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "?????????????????? ????????? ??????", error)

                    // ???????????? ???????????? ?????? ??? ???????????? ?????? ?????? ???????????? ???????????? ????????? ??????,
                    // ???????????? ????????? ????????? ?????? ????????????????????? ????????? ?????? ?????? ????????? ????????? ?????? (???: ?????? ??????)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // ??????????????? ????????? ?????????????????? ?????? ??????, ????????????????????? ????????? ??????
                    kakaoClient.loginWithKakaoAccount(this, callback = kakaoCallback)
                } else if (token != null) {
                    Log.i(TAG, "?????????????????? ????????? ?????? ${token.accessToken}")
                    viewModel.postTokenLogin(token.accessToken)
                }
            }
        } else {
            kakaoClient.loginWithKakaoAccount(this, callback = kakaoCallback)
        }

    }

    private fun firebaseKakaoLogin(firebaseToken: String){
        auth.signInWithCustomToken(firebaseToken)
            .addOnCompleteListener {
                Toast.makeText(this, "????????? ????????? ??????", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }.addOnFailureListener {

            }
    }

    private fun navigateToHome(){
        finishAffinity()
        startActivity(homeNavigator.navigate(this))
    }
}