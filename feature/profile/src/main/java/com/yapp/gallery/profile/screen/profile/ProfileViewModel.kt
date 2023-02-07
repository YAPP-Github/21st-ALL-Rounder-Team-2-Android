package com.yapp.gallery.profile.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.entity.profile.User
import com.yapp.gallery.domain.usecase.profile.GetUserUseCase
import com.yapp.gallery.domain.usecase.record.DeleteTempPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val deleteTempPostUseCase: DeleteTempPostUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _userData = MutableStateFlow<BaseState<User>>(BaseState.Loading)
    val userData : StateFlow<BaseState<User>> get() = _userData

    val loginType = sharedPreferences.getString("loginType", "").toString()

    init {
        getUser()
    }

    fun removeInfo(){
        // Todo : 임시 전시 정보 존재 시 삭제 하도록 변경 해야함
        viewModelScope.launch {
            deleteTempPostUseCase()
                .catch {  }
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
                }
                .collect{
                    _userData.value = BaseState.Success(it)
                }
        }
    }
}