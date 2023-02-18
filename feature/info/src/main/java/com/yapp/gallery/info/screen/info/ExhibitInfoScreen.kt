package com.yapp.gallery.info.screen.info

import android.view.ViewGroup
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
import com.yapp.gallery.common.util.WebViewUtils.cookieManager
import com.yapp.gallery.info.R
import com.yapp.gallery.info.utils.InfoNavigateJsObject
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExhibitInfoScreen(
    navigateToCamera: () -> Unit,
    navigateToGallery: () -> Unit,
    viewModel : ExhibitInfoViewModel = hiltViewModel()
){
    var webView : WebView? = null 
    val baseUrl = stringResource(id = R.string.exhibit_info_base_url)

    LaunchedEffect(viewModel.infoSideEffect){
        viewModel.infoSideEffect.collectLatest {
            when(it.action){
                "NAVIGATE_TO_CAMERA" -> {

                }
                "NAVIGATE_TO_GALLERY" -> {

                }
                else -> {

                }
            }
        }
    }

    LaunchedEffect(viewModel.idToken){
        viewModel.idToken.collectLatest {
            it?.let {
                webView?.loadUrl(baseUrl + viewModel.exhibitId, mapOf("Authorization" to it))
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
                    webViewClient = WebViewUtils.webViewClient
                    webChromeClient = WebViewUtils.webChromeClient
                    addJavascriptInterface(InfoNavigateJsObject { action, payload ->
                        viewModel.setInfoSideEffect(action, payload) }, "android")
                    settings.run {
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setAcceptThirdPartyCookies(webView, true)
                        javaScriptEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                    }
                }
            })
        }
    }
}