package com.yapp.gallery.info.screen.info

import android.app.Activity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.util.WebViewUtils
import com.yapp.gallery.common.util.webview.NavigateJsObject
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
    var webView : WebView? = null
    val baseUrl = stringResource(id = R.string.exhibit_info_base_url)

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

    LaunchedEffect(viewModel.idToken){
        viewModel.idToken.collectLatest {
            it?.let {
                webView?.loadUrl(baseUrl + exhibitId, mapOf("Authorization" to it))
            }
        }
    }


    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            AndroidView(factory = {
                WebView(it).apply {
                    webView = this
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
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
                        WebViewUtils.cookieManager.setAcceptThirdPartyCookies(webView, true)
                        javaScriptEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                    }
                }
            })
        }
    }
}
