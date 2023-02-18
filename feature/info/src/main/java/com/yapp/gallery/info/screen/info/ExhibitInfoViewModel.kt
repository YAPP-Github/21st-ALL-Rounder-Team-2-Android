package com.yapp.gallery.info.screen.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.yapp.gallery.info.utils.NavigatePayload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val exhibitId = savedStateHandle["exhibitId"] ?: 1
    val webViewState = WebViewState(
        WebContent.Url(
            url = "https://21st-all-rounder-team-2-web-bobeenlee.vercel.app/exhibition/${exhibitId}",
            additionalHttpHeaders = emptyMap()
        )
    )

    val webViewNavigator = WebViewNavigator(viewModelScope)

    private val _infoSideEffect = Channel<NavigatePayload>()
    val infoSideEffect = _infoSideEffect.receiveAsFlow()

    fun setInfoSideEffect(action: String, payload: String){
        viewModelScope.launch {
            _infoSideEffect.send(NavigatePayload(action, payload))
        }
    }
}