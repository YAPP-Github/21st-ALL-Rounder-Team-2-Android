package com.yapp.gallery.profile.screen.signout

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.usecase.profile.SignOutUseCase
import com.yapp.gallery.domain.usecase.record.DeleteBothUseCase
import com.yapp.gallery.profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val deleteBothUseCase: DeleteBothUseCase,
    private val sharedPreferences: SharedPreferences,
    private val googleSignInClient: GoogleSignInClient,
    private val kakaoClient: UserApiClient,
    private val auth: FirebaseAuth,
    @ApplicationContext val context: Context
) : ViewModel(){
    private val _signOutState = MutableStateFlow<SignOutState>(SignOutState.Initial)
    val signOutState : StateFlow<SignOutState>
        get() = _signOutState

    fun removeInfo(loginType: String){
        viewModelScope.launch {
            deleteBothUseCase()
                .catch {
                    signOut(loginType)
                }
                .collect{
                    signOut(loginType)
                }
        }
    }

    private fun signOut(loginType: String){
        viewModelScope.launch {
            signOutUseCase()
                .catch {
                    // 회원 탈퇴 실패
                    _signOutState.value = SignOutState.Failure(UiText.StringResource(R.string.sign_out_error))
                }
                .collectLatest {
                    if (it){
                        sharedPreferences.edit().apply {
                            remove("idToken").apply()
                            remove("loginType").apply()
                        }
                        signOutFromSocial(loginType)
                        // 회원 탈퇴 성공
                        _signOutState.value = SignOutState.Success
                    }
                    else{
                        _signOutState.value = SignOutState.Failure(UiText.StringResource(R.string.sign_out_error))
                    }
                }
        }
    }

    private fun signOutFromSocial(loginType: String){
        Log.e("loginType", loginType)
        when(loginType){
            "kakao" -> {
                kakaoClient.unlink {
                    auth.currentUser?.delete()
                }
            }
            "naver" -> {
                NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
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

}