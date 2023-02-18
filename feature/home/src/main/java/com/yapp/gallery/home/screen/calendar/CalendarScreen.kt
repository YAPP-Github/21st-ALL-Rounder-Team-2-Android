package com.yapp.gallery.home.screen.calendar

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.util.WebViewUtils
import com.yapp.gallery.home.utils.NavigateJsObject

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
){
    // Todo : 캘린더 화면 오류 수정 시 반영
    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
//            AndroidView(factory = {
//                WebView(it).apply {
//                    webView = this
//                    layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                    )
//                    webViewClient = WebViewUtils.webViewClient
//                    webChromeClient = WebViewUtils.webChromeClient
//                    addJavascriptInterface(
//                        NavigateJsObject { e -> viewModel.setSideEffect(e) }, "android")
//                    settings.run {
//                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//                        WebViewUtils.cookieManager.setAcceptCookie(true)
//                        WebViewUtils.cookieManager.setAcceptThirdPartyCookies(webView, true)
//                        javaScriptEnabled = true
//                        javaScriptCanOpenWindowsAutomatically = true
//                    }
//                }
//            })
        }
    }
}