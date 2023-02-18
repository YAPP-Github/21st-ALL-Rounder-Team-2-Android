package com.yapp.gallery.home.screen.home

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebSettings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import com.google.accompanist.web.WebView
import com.yapp.gallery.common.util.WebViewUtils.cookieManager
import com.yapp.gallery.common.util.WebViewUtils.webChromeClient
import com.yapp.gallery.common.util.WebViewUtils.webViewClient
import com.yapp.gallery.home.utils.NavigateJsObject

@SuppressLint("RequiresFeature")
@Composable
fun HomeScreen(
    navigateToRecord: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToInfo: (Long) -> Unit,
){
    val viewModel = hiltViewModel<HomeViewModel>()

    LaunchedEffect(viewModel.homeSideEffect){
        viewModel.homeSideEffect.collect {
            when(it){
                "NAVIGATE_TO_EDIT" -> navigateToRecord()
                "NAVIGATE_TO_MY" -> navigateToProfile()
                "NAVIGATE_TO_CALENDAR" -> navigateToInfo(12)
                // Todo : 임시
                else -> navigateToInfo(12)
            }
        }
    }

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            WebView(
                state = viewModel.webViewState,
                client = webViewClient,
                chromeClient = webChromeClient,
                navigator = viewModel.webViewNavigator,
                onCreated = {
                    with(it){
                        addJavascriptInterface(
                            NavigateJsObject { e -> viewModel.setSideEffect(e) }, "android")
                        settings.run {
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            cookieManager.setAcceptCookie(true)
                            cookieManager.setAcceptThirdPartyCookies(it, true)
                            javaScriptEnabled = true
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

