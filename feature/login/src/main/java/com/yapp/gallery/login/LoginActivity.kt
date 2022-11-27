package com.yapp.gallery.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity(){
    @Inject lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient : SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    // private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOneTap()
        initResultLauncher()
        setContent {
            MaterialTheme {
                LoginScreen(googleLogin = {oneTapGoogleSignIn()})
            }
        }
    }

    private fun initOneTap(){
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.FIREBASE_WEB_CLIENT_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()
    }

    private fun initResultLauncher(){
//        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            if (it.resultCode == RESULT_OK){
//                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//                try{
//                    val account = task.getResult(ApiException::class.java)
//                    Log.e("account", "${account.id}, ${account.email}, ${account.givenName}")
//                    firebaseAuthWithGoogle(account)
//                } catch (e: ApiException){
//                    Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        googleResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
            if (it.resultCode == RESULT_OK){
                val credential = oneTapClient.getSignInCredentialFromIntent(it.data)
                val idToken = credential.googleIdToken
                val username = credential.id
                idToken?.let {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success")
                                // Todo : 유저 정보 넘기기
                                val user = auth.currentUser
                                Log.e("Login", user?.uid.toString())
                                Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_SHORT).show()

                                // Todo : Navigation 이용
//                                startActivity(Intent(this, com.yapp.gallery.home.home.HomeActivity::class.java))
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.exception)
                            }
                        }
                }
            }
        }
    }
    // 구글 로그인 객체 생성
//    fun googleLogin(){
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(resources.getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        googleSignIn()
//    }

    fun oneTapGoogleSignIn(){
        Log.e(TAG, "One Tap 시작")
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    googleResultLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException){
                    Log.e(TAG, "One Tap UI 실패 : ${e.localizedMessage}")
                }
            }.addOnFailureListener(this) {  e->
                Log.e(TAG, "One Tap UI 실패 : ${e.localizedMessage}")
            }
    }
//    private fun googleSignIn(){
//        val signInIntent = googleSignInClient.signInIntent
//        activityResultLauncher.launch(signInIntent)
//    }

//    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
//        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}