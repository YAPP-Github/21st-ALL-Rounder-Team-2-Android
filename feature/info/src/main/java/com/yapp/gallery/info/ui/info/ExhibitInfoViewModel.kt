package com.yapp.gallery.info.ui.info

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.util.webview.NavigatePayload
import com.yapp.gallery.common.util.webview.WebViewState
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import com.yapp.gallery.domain.usecase.auth.GetValidTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitInfoViewModel @Inject constructor(
    private val getRefreshedTokenUseCase: GetRefreshedTokenUseCase
) : ViewModel() {

    private val _infoState = MutableStateFlow<WebViewState>(WebViewState.Initial)
    val infoState : StateFlow<WebViewState>
        get() = _infoState

    private val _infoSideEffect = Channel<NavigatePayload>()
    val infoSideEffect = _infoSideEffect.receiveAsFlow()

    init {
        getRefreshedToken()
    }

    fun getRefreshedToken(){
        viewModelScope.launch {
            runCatching { getRefreshedTokenUseCase() }
                .onSuccess {
                    _infoState.value = WebViewState.Connected(it)
                }
                .onFailure {
                    _infoState.value = WebViewState.Disconnected
                }
        }
    }

    fun setInfoSideEffect(action: String, payload: String?){
        viewModelScope.launch {
            _infoSideEffect.send(NavigatePayload(action, payload))
        }
    }
}