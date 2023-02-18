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
<<<<<<< HEAD
import com.yapp.gallery.common.util.WebViewUtils
import com.yapp.gallery.home.utils.NavigateJsObject
=======
>>>>>>> 088001c ([ Refactor ] : Webview 변경 및 Authorization Header 추가)

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
){
<<<<<<< HEAD
    // Todo : 캘린더 화면 오류 수정 시 반영
=======
>>>>>>> 088001c ([ Refactor ] : Webview 변경 및 Authorization Header 추가)
    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
<<<<<<< HEAD
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
=======
//            WebView(
//                state = viewModel.webViewState,
//                client = webViewClient,
//                chromeClient = webChromeClient,
//                navigator = viewModel.webViewNavigator,
//                onCreated = {
//                    with(it){
//                        settings.run {
//                            javaScriptEnabled = true
//                        }
//                    }
//                },
//            )
>>>>>>> 088001c ([ Refactor ] : Webview 변경 및 Authorization Header 추가)
        }
    }
}