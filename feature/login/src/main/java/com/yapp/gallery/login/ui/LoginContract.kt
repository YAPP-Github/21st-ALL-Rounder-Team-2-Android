package com.yapp.gallery.login.ui

import com.yapp.gallery.common.base.ViewModelContract

class LoginContract {
    sealed class LoginState : ViewModelContract.State{
        object Initial : LoginState()
        object Loading : LoginState()
        data class TokenSuccess(val token: String) : LoginState()
        data class TokenError(val message: String?) : LoginState()
        data class LoginSuccess(val id: Long) : LoginState()
        data class LoginError(val message: String?) : LoginState()
    }

    sealed class LoginEvent : ViewModelContract.Event{
        object OnGoogleLogin : LoginEvent()
        object OnKakaoLogin : LoginEvent()
        object OnNaverLogin : LoginEvent()
        data class OnLoginFailure(val message: String?) : LoginEvent()
        data class OnCreateGoogleUser(val firebaseId: String, val idToken: String) : LoginEvent()
        data class OnCreateKakaoUser(val accessToken: String) : LoginEvent()
        data class OnCreateNaverUser(val accessToken: String) : LoginEvent()
    }

    sealed class LoginSideEffect : ViewModelContract.SideEffect{
        object LaunchGoogleLauncher : LoginSideEffect()
        object LaunchKakaoLauncher : LoginSideEffect()
        object LaunchNaverLauncher : LoginSideEffect()
        object NavigateToHome : LoginSideEffect()
    }

    sealed class LoginType {
        object None : LoginType()
        object Google : LoginType()
        object Kakao : LoginType()
        object Naver : LoginType()
    }
}