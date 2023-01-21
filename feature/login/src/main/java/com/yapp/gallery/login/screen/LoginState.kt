package com.yapp.gallery.login.screen

sealed class TokenState {
    object None : TokenState()
    data class Success(val token: String) : TokenState()
    object Failure : TokenState()
}

sealed class LoginState{
    object None : LoginState()
    object Loading : LoginState()
    data class Success(val userId: Long) : LoginState()
    object Failure : LoginState()
}
