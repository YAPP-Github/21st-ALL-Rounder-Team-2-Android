package com.yapp.gallery.profile.screen.nickname

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val originNickname = savedStateHandle["nickname"] ?: ""

    val nickname = mutableStateOf(originNickname)

    private val _nicknameState = MutableStateFlow<NicknameState>(NicknameState.None)
    val nicknameState : StateFlow<NicknameState>
        get() = _nicknameState

    fun changeNickname(data: String){
        nickname.value = data
        if (data == originNickname || data.isEmpty()){
            _nicknameState.value = NicknameState.None
        } else if (data.length in 1..10){
            _nicknameState.value = NicknameState.Normal(data)
        } else {
            _nicknameState.value = NicknameState.Error("닉네임은 10자 이하이어야 합니다.")
        }
    }
}