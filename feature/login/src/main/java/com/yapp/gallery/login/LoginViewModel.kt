package com.yapp.gallery.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.domain.usecase.login.CreateUserUseCase
import com.yapp.gallery.domain.usecase.login.PostTokenLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.mutable.Mutable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenLoginUseCase: PostTokenLoginUseCase,
    private val createUserUseCase: CreateUserUseCase
): ViewModel(){
    private var _tokenState = MutableStateFlow<TokenState>(TokenState.None)
    val tokenState : StateFlow<TokenState>
        get() = _tokenState

    private var _loginState = MutableStateFlow<LoginState>(LoginState.None)
    val loginState : StateFlow<LoginState>
        get() = _loginState

    fun postTokenLogin(accessToken: String){
        Log.e("login 전", accessToken)
        viewModelScope.launch {
            runCatching { tokenLoginUseCase(accessToken) }
                .onSuccess {
                    _tokenState.value = TokenState.Success(it)
                }
                .onFailure {
                    Log.e("login 오류", it.message.toString())
                    _tokenState.value = TokenState.Failure
                }
        }
    }

    fun setLoading(){
        _loginState.value = LoginState.Loading
    }

    fun createUser(firebaseUserId: String) {
        viewModelScope.launch {
            runCatching { createUserUseCase(firebaseUserId) }
                .onSuccess {
                    _loginState.value = LoginState.Success(it)
                }
                .onFailure {
                    Log.e("error", it.message.toString())
                    _loginState.value = LoginState.Failure
                }
        }
    }
}
