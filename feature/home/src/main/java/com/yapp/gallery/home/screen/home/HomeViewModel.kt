package com.yapp.gallery.home.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    // Web View State
    private var _homeSideEffect = MutableStateFlow("")
    val homeSideEffect : StateFlow<String>
        get() = _homeSideEffect

    val webViewState = WebViewState(
        WebContent.Url(
            url = "https://21st-all-rounder-team-2-web-bobeenlee.vercel.app/home",
            additionalHttpHeaders = emptyMap()
        )
    )

    val webViewNavigator = WebViewNavigator(viewModelScope)

    fun setSideEffect(action: String){
        _homeSideEffect.value = action
    }
    fun clearSideEffect(){
        _homeSideEffect.value = ""
    }
}