package com.yapp.gallery.login.ui

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.common.base.BaseStateViewModel
import com.yapp.gallery.domain.usecase.auth.SetLoginInfoUseCase
import com.yapp.gallery.domain.usecase.login.CreateUserUseCase
import com.yapp.gallery.domain.usecase.login.PostKakaoLoginUseCase
import com.yapp.gallery.domain.usecase.login.PostNaverLoginUseCase
import com.yapp.gallery.login.ui.LoginContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val tokenKakaoLoginUseCase: PostKakaoLoginUseCase,
    private val tokenNaverLoginUseCase: PostNaverLoginUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val setLoginInfoUseCase: SetLoginInfoUseCase,
) : BaseStateViewModel<LoginState, LoginEvent, LoginSideEffect>() {
    override val _viewState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Initial)

    private val _loginType : MutableStateFlow<LoginType> = MutableStateFlow(LoginType.None)

    val isLoading = _viewState.map {
        it is LoginState.Loading || it is LoginState.LoginSuccess || it is LoginState.TokenSuccess
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private fun postKakaoLogin(accessToken: String) {
        setViewState(LoginState.Loading)
        tokenKakaoLoginUseCase(accessToken)
            .onEach {
                setViewState(LoginState.TokenSuccess(it))
                _loginType.value = LoginType.Kakao
                firebaseTokenLogin(it)
            }
            .catch {
                Timber.e("Login 오류 : ${it.message}")
                setViewState(LoginState.TokenError(it.message))
            }
            .launchIn(viewModelScope)
    }

    private fun postNaverLogin(accessToken: String) {
        setViewState(LoginState.Loading)
        tokenNaverLoginUseCase(accessToken)
            .onEach {
                setViewState(LoginState.TokenSuccess(it))
                _loginType.value = LoginType.Naver
                firebaseTokenLogin(it)
            }
            .catch {
                Timber.e("Login 오류 : ${it.message}")
                setViewState(LoginState.TokenError(it.message))
            }
            .launchIn(viewModelScope)
    }

    // 카카오, 네이버 로그인
    private fun firebaseTokenLogin(firebaseToken: String) {
        auth.signInWithCustomToken(firebaseToken)
            .addOnCompleteListener { task ->
                task.result.user?.apply {
                    getIdToken(false).addOnCompleteListener { t ->
                        setLoginInfo(uid, t.result?.token ?: "")
                    }
                }
            }.addOnFailureListener {
                setViewState(LoginState.LoginError(it.message))
            }
    }



    private fun createUser(firebaseUserId: String) {
        createUserUseCase(firebaseUserId)
            .onEach {
                sendSideEffect(LoginSideEffect.NavigateToHome)
                setViewState(LoginState.LoginSuccess(it))
            }
            .catch {
                Timber.e("Login 오류 : ${it.message}")
                setViewState(LoginState.LoginError(it.message))
            }
            .launchIn(viewModelScope)
    }

    private fun setLoginInfo(firebaseId: String, token : String){
        setLoginInfoUseCase(getLoginTypeToString(), token)
            .onEach { createUser(firebaseId) }
            .launchIn(viewModelScope)
    }

    override fun handleEvents(event: LoginEvent) {
        when(event){
            is LoginEvent.OnGoogleLogin -> {
                if (!isLoading.value) {
                    sendSideEffect(LoginSideEffect.LaunchGoogleLauncher)
                }
            }
            is LoginEvent.OnKakaoLogin -> {
                if (!isLoading.value) {
                    sendSideEffect(LoginSideEffect.LaunchKakaoLauncher)
                }
            }
            is LoginEvent.OnNaverLogin -> {
                if (!isLoading.value) {
                    sendSideEffect(LoginSideEffect.LaunchNaverLauncher)
                }
            }
            is LoginEvent.OnLoginFailure -> {
                setViewState(LoginState.TokenError(event.message))
            }
            is LoginEvent.OnCreateGoogleUser -> {
                setViewState(LoginState.Loading)
                _loginType.value = LoginType.Google
                setLoginInfo(event.firebaseId, event.idToken)
            }
            is LoginEvent.OnCreateKakaoUser -> {
                postKakaoLogin(event.accessToken)
            }
            is LoginEvent.OnCreateNaverUser -> {
                postNaverLogin(event.accessToken)
            }
        }
    }

    private fun getLoginTypeToString() : String{
        return when(_loginType.value){
            LoginType.Google -> "google"
            LoginType.Kakao -> "kakao"
            LoginType.Naver -> "naver"
            LoginType.None -> "none"
        }
    }
}
