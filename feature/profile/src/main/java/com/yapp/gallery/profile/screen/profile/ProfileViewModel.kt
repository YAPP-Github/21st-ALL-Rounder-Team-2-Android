package com.yapp.gallery.profile.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.entity.profile.User
import com.yapp.gallery.domain.usecase.profile.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _loginType = MutableStateFlow("")
    val loginType : StateFlow<String>
        get() = _loginType

    private var _userData = MutableStateFlow<BaseState<User>>(BaseState.Loading)
    val userData : StateFlow<BaseState<User>> get() = _userData

    init {
        getUser()
    }

    fun logout(){
        val type = sharedPreferences.getString("loginType", "").toString()
        sharedPreferences.edit().apply {
            remove("idToken")
            remove("loginType")
        }.apply()
        _loginType.value = type
    }

    private fun getUser(){
        viewModelScope.launch {
            getUserUseCase()
                .catch {
                    _userData.value = BaseState.Error(it.message)
                }
                .collect{
                    _userData.value = BaseState.Success(it)
                }
        }
    }
}