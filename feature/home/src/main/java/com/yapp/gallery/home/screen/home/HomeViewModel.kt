package com.yapp.gallery.home.screen.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private val idToken = sharedPreferences.getString("idToken", "")
    private var _homeSideEffect = Channel<String>()
    val homeSideEffect = _homeSideEffect.receiveAsFlow()

    val webViewState = WebViewState(
        WebContent.Url(
            url = "https://21st-all-rounder-team-2-web-bobeenlee.vercel.app/home",
            additionalHttpHeaders = if (idToken != null) mapOf("Authorization" to idToken) else emptyMap()
        )

    )

    val webViewNavigator = WebViewNavigator(viewModelScope)

    fun setSideEffect(action: String){
        viewModelScope.launch {
            _homeSideEffect.send(action)
        }
        Log.e("homeSideEffect", action)
    }
}