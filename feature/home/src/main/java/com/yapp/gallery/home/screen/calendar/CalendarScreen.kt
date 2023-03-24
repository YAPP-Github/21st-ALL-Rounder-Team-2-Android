package com.yapp.gallery.home.screen.calendar

import android.app.Activity
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.util.WebViewUtils
import com.yapp.gallery.common.util.webview.NavigateJsObject
import com.yapp.gallery.common.util.webview.WebViewState
import com.yapp.gallery.home.R

@Composable
fun CalendarScreen(
    popBackStack: () -> Unit,
    context: Activity,
    viewModel: CalendarViewModel = hiltViewModel()
){
    LaunchedEffect(viewModel.calendarSideEffect){
        viewModel.calendarSideEffect.collect {
            when(it.action){
                "GO_BACK" -> popBackStack()
                else -> {}
            }
        }
    }

    val webView = WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        addJavascriptInterface(NavigateJsObject { action, payload ->
            viewModel.setSideEffect(action, payload)
        }, "android")
        webViewClient = WebViewUtils.webViewClient
        webChromeClient = WebViewUtils.webChromeClient
        settings.run {
            setBackgroundColor(0)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            WebViewUtils.cookieManager.setAcceptCookie(true)
            WebViewUtils.cookieManager.setAcceptThirdPartyCookies(this@apply, true)
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
    }

    CalendarWebView(
        webView = webView, 
        calendarState = viewModel.calendarState.collectAsState().value,
        onReload = { viewModel.getRefreshedToken() }
    ) 
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CalendarWebView(
    webView: WebView,
    calendarState: WebViewState,
    onReload : () -> Unit
){
    val baseUrl = stringResource(id = R.string.calendar_base_url)
    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (calendarState is WebViewState.Disconnected){
                Text(
                    text = stringResource(id = R.string.home_network_error),
                    style = MaterialTheme.typography.h3.copy(
                        color = color_gray600,
                        lineHeight = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                // 네트워크 재요청 버튼
                Surface(shape = RoundedCornerShape(71.dp),
                    color = MaterialTheme.colors.background,
                    border = BorderStroke(1.dp, color = Color(0xFFA7C5F9)),
                    onClick = onReload
                ) {
                    Text(
                        text = stringResource(id = R.string.home_network_reload_btn),
                        style = MaterialTheme.typography.h3.copy(
                            color = Color(0xFFA7C5F9), fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(
                            horizontal = 24.dp, vertical = 12.dp
                        )
                    )
                }

            }
            else {
                AndroidView(
                    factory = { webView },
                    update = {
                        if (calendarState is WebViewState.Connected){
                            it.loadUrl(baseUrl, mapOf("Authorization" to calendarState.idToken))
                        }
                    }
                )
            }
        }
    }
}