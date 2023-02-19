package com.yapp.gallery.home.screen.home

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
import com.yapp.gallery.common.util.WebViewUtils.cookieManager
import com.yapp.gallery.home.R
import com.yapp.gallery.home.utils.NavigateJsObject
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navigateToRecord: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToInfo: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
){
    var webView: WebView? = null
    val baseUrl = stringResource(id = R.string.home_base_url)

    val viewModel = hiltViewModel<HomeViewModel>()

    LaunchedEffect(viewModel.homeSideEffect){
        viewModel.homeSideEffect.collect {
            when(it){
                "NAVIGATE_TO_EDIT" -> navigateToRecord()
                "NAVIGATE_TO_MY" -> navigateToProfile()
                "NAVIGATE_TO_CALENDAR" -> navigateToInfo(19)
                // Todo : 임시
                else -> navigateToInfo(19)
            }
        }
    }

    LaunchedEffect(viewModel.idToken){
        viewModel.idToken.collectLatest {
            it?.let {
                webView?.loadUrl(baseUrl, mapOf("Authorization" to it))
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewUtils.webViewClient
                    webChromeClient = WebViewUtils.webChromeClient
                    addJavascriptInterface(
                        NavigateJsObject { e -> viewModel.setSideEffect(e) }, "android")
                    settings.run {
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setAcceptThirdPartyCookies(webView, true)
                        javaScriptEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
            )
        }
    }
}

