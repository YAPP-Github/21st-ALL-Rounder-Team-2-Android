package com.yapp.gallery.profile.screen.profile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.entity.profile.User
import com.yapp.gallery.domain.usecase.profile.GetUserUseCase
import com.yapp.gallery.domain.usecase.record.DeleteBothUseCase
import com.yapp.gallery.profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val deleteBothUseCase: DeleteBothUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _userData = MutableStateFlow<BaseState<User>>(BaseState.Loading)
    val userData : StateFlow<BaseState<User>> get() = _userData

    private var _errorChannel = Channel<UiText>()
    val errors = _errorChannel.receiveAsFlow()

    init {
        getUser()
    }

    fun removeInfo(){
        viewModelScope.launch {
            deleteBothUseCase()
                .catch {
                    Log.e("removeProfile", it.message.toString())
                }
                .collect()
        }

        sharedPreferences.edit().apply {
            remove("idToken").apply()
            remove("loginType").apply()
        }
    }

    private fun getUser(){
        viewModelScope.launch {
            getUserUseCase()
                .catch {
                    _userData.value = BaseState.Error(it.message)
                    _errorChannel.send(UiText.StringResource(R.string.profile_load_error))
                }
                .collect{
                    _userData.value = BaseState.Success(it)
                }
        }
    }
}