package com.yapp.gallery.common.util.webview

sealed class WebViewState{
    object Initial : WebViewState()
    object Disconnected : WebViewState()
    data class Connected(val idToken : String) : WebViewState()
}
