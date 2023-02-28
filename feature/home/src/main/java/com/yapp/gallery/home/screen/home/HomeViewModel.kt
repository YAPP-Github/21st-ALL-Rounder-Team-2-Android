package com.yapp.gallery.home.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.util.webview.NavigatePayload
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getRefreshedTokenUseCase: GetRefreshedTokenUseCase,
) : ViewModel() {
    private var _homeSideEffect = Channel<NavigatePayload>()
    val homeSideEffect = _homeSideEffect.receiveAsFlow()

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken : StateFlow<String?>
        get() = _idToken

    init {
        getRefreshedTokenUseCase()
            .onEach { _idToken.value = it }
            .launchIn(viewModelScope)
    }


    fun setSideEffect(action: String, payload: String?){
        viewModelScope.launch {
            _homeSideEffect.send(NavigatePayload(action, payload))
        }
        Log.e("homeSideEffect", action)
    }
}