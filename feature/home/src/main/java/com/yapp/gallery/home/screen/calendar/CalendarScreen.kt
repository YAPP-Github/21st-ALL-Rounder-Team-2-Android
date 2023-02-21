package com.yapp.gallery.home.screen.calendar

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.util.WebViewUtils
import com.yapp.gallery.home.R
import com.yapp.gallery.home.utils.NavigateJsObject
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CalendarScreen(
    popBackStack: () -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
){
    val baseUrl = stringResource(id = R.string.calendar_base_url)
    var webView : WebView? = null

    LaunchedEffect(viewModel.idToken){
        viewModel.idToken.collectLatest {
            it?.let {
                webView?.loadUrl(baseUrl, mapOf("Authorization" to it))
            }
        }
    }

    LaunchedEffect(viewModel.calendarSideEffect){
        viewModel.calendarSideEffect.collect {
            when(it.action){
                "GO_BACK" -> popBackStack()
                else -> {}
            }
        }
    }

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AndroidView(factory = {
                WebView(it).apply {
                    webView = this
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    addJavascriptInterface(NavigateJsObject {
                        viewModel.setSideEffect(it)
                    }, "android")
                    webViewClient = WebViewUtils.webViewClient
                    webChromeClient = WebViewUtils.webChromeClient
                    settings.run {
                        setBackgroundColor(0)
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        WebViewUtils.cookieManager.setAcceptCookie(true)
                        WebViewUtils.cookieManager.setAcceptThirdPartyCookies(webView, true)
                        javaScriptEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                    }
                }
            })
        }
    }
}