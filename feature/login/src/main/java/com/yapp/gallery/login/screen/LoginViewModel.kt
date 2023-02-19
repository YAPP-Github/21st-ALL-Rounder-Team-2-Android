package com.yapp.gallery.login.screen

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.usecase.login.CreateUserUseCase
import com.yapp.gallery.domain.usecase.login.PostKakaoLoginUseCase
import com.yapp.gallery.domain.usecase.login.PostNaverLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenKakaoLoginUseCase: PostKakaoLoginUseCase,
    private val tokenNaverLoginUseCase: PostNaverLoginUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val sharedPreferences: SharedPreferences
): ViewModel(){
    private var _tokenState = MutableStateFlow<BaseState<String>>(BaseState.None)
    val tokenState : StateFlow<BaseState<String>>
        get() = _tokenState

    private var _loginState = MutableStateFlow<BaseState<Long>>(BaseState.None)
    val loginState : StateFlow<BaseState<Long>>
        get() = _loginState

    fun postKakaoLogin(accessToken: String){
        Log.e("login 전", accessToken)
        setLoading()
        viewModelScope.launch {
            runCatching { tokenKakaoLoginUseCase(accessToken) }
                .onSuccess {
                    _tokenState.value = BaseState.Success(it)
                }
                .onFailure {
                    Log.e("login 오류", it.message.toString())
                    _tokenState.value = BaseState.Error(it.message)
                }
        }
    }

    fun postNaverLogin(accessToken: String){
        setLoading()
        viewModelScope.launch {
            runCatching { tokenNaverLoginUseCase(accessToken) }
                .onSuccess {
                    _tokenState.value = BaseState.Success(it)
                }
                .onFailure {
                    Log.e("login 오류", it.message.toString())
                    _tokenState.value = BaseState.Error(it.message)
                }
        }
    }

    fun setLoading(){
        _loginState.value = BaseState.Loading
    }

    fun createUser(firebaseUserId: String) {
        viewModelScope.launch {
            runCatching { createUserUseCase(firebaseUserId) }
                .onSuccess {
                    _loginState.value = BaseState.Success(it)
                    sharedPreferences.edit().putLong("uid", it).apply()
                }
                .onFailure {
                    Log.e("error", it.message.toString())
                    _loginState.value = BaseState.Error(it.message)
                }
        }
    }
}
