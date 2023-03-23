package com.yapp.gallery.home.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.util.webview.NavigatePayload
import com.yapp.gallery.common.util.webview.WebViewState
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRefreshedTokenUseCase: GetRefreshedTokenUseCase,
) : ViewModel() {
    private var _homeSideEffect = Channel<NavigatePayload>()
    val homeSideEffect = _homeSideEffect.receiveAsFlow()

    private val _homeState = MutableStateFlow<WebViewState>(WebViewState.Initial)
    val homeState : StateFlow<WebViewState>
        get() = _homeState

    init {
        getRefreshedToken()
    }

    fun getRefreshedToken(){
        getRefreshedTokenUseCase()
            .catch {
                Log.e("error", it.message.toString())
                _homeState.value = WebViewState.Disconnected
            }
            .onEach {
                Log.e("success", it)
                _homeState.value = WebViewState.Connected(it)
            }
            .launchIn(viewModelScope)
    }


    fun setSideEffect(action: String, payload: String?){
        viewModelScope.launch {
            _homeSideEffect.send(NavigatePayload(action, payload))
        }
        Log.e("homeSideEffect", action)
    }
}