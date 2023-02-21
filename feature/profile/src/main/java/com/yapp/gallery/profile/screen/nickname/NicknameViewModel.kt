package com.yapp.gallery.profile.screen.nickname

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.usecase.profile.UpdateNicknameUseCase
import com.yapp.gallery.profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    sharedPreferences: SharedPreferences
) : ViewModel(){
    private val userId = sharedPreferences.getLong("uid", 2)
    private val originNickname = savedStateHandle["nickname"] ?: ""

    val nickname = mutableStateOf(originNickname)

    private val _nicknameState = MutableStateFlow<NicknameState>(NicknameState.None)
    val nicknameState : StateFlow<NicknameState>
        get() = _nicknameState

    fun changeNickname(editedName: String){
        nickname.value = editedName
        if (editedName == originNickname || editedName.isEmpty()){
            _nicknameState.value = NicknameState.None
        } else if (editedName.length in 1..10){
            _nicknameState.value = NicknameState.Normal(editedName)
        } else {
            _nicknameState.value = NicknameState.Error("닉네임은 10자 이하이어야 합니다.")
        }
    }

    fun updateNickname(){
        viewModelScope.launch {
            updateNicknameUseCase(userId, nickname.value)
                .catch {
                }
                .collectLatest {
                }
        }
    }
}