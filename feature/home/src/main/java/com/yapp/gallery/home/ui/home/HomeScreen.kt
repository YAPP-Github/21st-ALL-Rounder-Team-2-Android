package com.yapp.gallery.home.ui.home

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
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.gallery.home.ui.home.HomeContract.*
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.util.WebViewUtils
import com.yapp.gallery.common.util.WebViewUtils.cookieManager
import com.yapp.gallery.common.util.webview.NavigateJsObject
import com.yapp.gallery.home.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    navigateToRecord: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToInfo: (Long) -> Unit,
    context: Activity,
    viewModel: HomeViewModel = hiltViewModel()
){
    val homeState : HomeState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect){
        viewModel.sideEffect.collectLatest {
            when(it){
                is HomeSideEffect.NavigateToRecord -> navigateToRecord()
                is HomeSideEffect.NavigateToProfile -> navigateToProfile()
                is HomeSideEffect.NavigateToCalendar -> navigateToCalendar()
                is HomeSideEffect.NavigateToInfo -> navigateToInfo(it.postId)
            }
        }
    }

    val webView = WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webViewClient = WebViewUtils.webViewClient
        webChromeClient = WebViewUtils.webChromeClient
        addJavascriptInterface(
            NavigateJsObject { action, payload -> viewModel.setEvent(HomeEvent.OnWebViewClick(action, payload)) }
            , "android")
        settings.run {
            setBackgroundColor(0)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(this@apply, true)
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = false
        }
    }

    HomeScreen(
        homeState = homeState,
        webView = webView,
        onReload = { viewModel.setEvent(HomeEvent.OnLoadAgain) }
    )
}

@Composable
private fun HomeScreen(
    homeState : HomeState,
    webView : WebView,
    onReload : () -> Unit
){
    val baseUrl = stringResource(id = R.string.home_base_url)

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (homeState is HomeState.Disconnected){
                HomeDisconnectedScreen(onReload)
            } else {
                AndroidView(
                    factory = { webView },
                    update = {
                        if (homeState is HomeState.Connected) {
                            it.loadUrl(baseUrl, mapOf("Authorization" to homeState.idToken))
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeDisconnectedScreen(
    onReload : () -> Unit
){
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
