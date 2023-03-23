package com.yapp.gallery.info.ui.info

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.yapp.gallery.info.R
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ExhibitInfoScreen(
    exhibitId: Long,
    navigateToCamera: () -> Unit,
    navigateToGallery: () -> Unit,
    navigateToEdit: (String) -> Unit,
    popBackStack: () -> Unit,
    viewModel: ExhibitInfoViewModel = hiltViewModel(),
    context: Activity,
){
    LaunchedEffect(viewModel.infoSideEffect){
        viewModel.infoSideEffect.collectLatest {
            when(it.action){
                "NAVIGATE_TO_EXHIBIT_EDIT" -> {
                    it.payload?.let { p ->
                        navigateToEdit(p)
                    }
                }
                "NAVIGATE_TO_CAMERA" -> {
                    navigateToCamera()
                }
                "NAVIGATE_TO_GALLERY" -> {
                    navigateToGallery()
                }
                "GO_BACK" -> {
                    popBackStack()
                }
                else -> {

                }
            }
        }
    }

    // 전체 화면 및 상태바 투명화
    LaunchedEffect(Unit){
        context.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    // 웹뷰 정의
    val webView = WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                if (this.canGoBack()) {
                    this.goBack()
                } else {
                    popBackStack()
                }
            }
            return@setOnKeyListener true
        }
        webViewClient = WebViewUtils.webViewClient
        webChromeClient = WebViewUtils.webChromeClient
        addJavascriptInterface(NavigateJsObject { action, payload ->
            viewModel.setInfoSideEffect(action, payload)
        }, "android")
        settings.run {
            setBackgroundColor(0)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            WebViewUtils.cookieManager.setAcceptCookie(true)
            WebViewUtils.cookieManager.setAcceptThirdPartyCookies(this@apply, true)
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
    }

    InfoWebView(
        exhibitId = exhibitId,
        webView = webView,
        infoState = viewModel.infoState.collectAsState().value,
        onReload = { viewModel.getRefreshedToken() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun InfoWebView(
    exhibitId : Long,
    webView: WebView,
    infoState: WebViewState,
    onReload: () -> Unit,
){
    val baseUrl = stringResource(id = R.string.exhibit_info_base_url)

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (infoState is WebViewState.Disconnected){
                Text(
                    text = stringResource(id = com.yapp.gallery.home.R.string.home_network_error),
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
                    onClick = onReload ) {
                    Text(
                        text = stringResource(id = com.yapp.gallery.home.R.string.home_network_reload_btn),
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
                        if (infoState is WebViewState.Connected){
                            it.loadUrl(baseUrl + exhibitId, mapOf("Authorization" to infoState.idToken))
                        }
                    }
                )
            }
        }
    }
}