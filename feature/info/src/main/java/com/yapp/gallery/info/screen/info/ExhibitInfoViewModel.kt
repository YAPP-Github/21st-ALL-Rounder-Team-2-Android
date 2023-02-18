package com.yapp.gallery.info.screen.info

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import com.yapp.gallery.info.utils.NavigatePayload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitInfoViewModel @Inject constructor(
    getRefreshedTokenUseCase: GetRefreshedTokenUseCase,
    sharedPreferences: SharedPreferences,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val exhibitId = savedStateHandle["exhibitId"] ?: 1

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken : StateFlow<String?>
        get() = _idToken

    init {
        viewModelScope.launch {
            getRefreshedTokenUseCase()
                .collect{
                    _idToken.value = it
                    it?.let {
                        sharedPreferences.edit().putString("idToken", it).apply()
                    }
                }
        }
    }

    private val _infoSideEffect = Channel<NavigatePayload>()
    val infoSideEffect = _infoSideEffect.receiveAsFlow()

    fun setInfoSideEffect(action: String, payload: String){
        viewModelScope.launch {
            _infoSideEffect.send(NavigatePayload(action, payload))
        }
    }
}