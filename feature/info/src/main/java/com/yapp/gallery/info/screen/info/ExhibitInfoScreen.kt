package com.yapp.gallery.info.screen.info

<<<<<<< HEAD
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
=======
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
<<<<<<< HEAD
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
    
    
=======
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.WebView
import com.yapp.gallery.common.util.WebViewClient.webChromeClient
import com.yapp.gallery.common.util.WebViewClient.webViewClient

@Composable
fun ExhibitInfoScreen(
    viewModel : ExhibitInfoViewModel = hiltViewModel()
){
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
<<<<<<< HEAD
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
=======
            WebView(
                state = viewModel.webViewState,
                client = webViewClient,
                chromeClient = webChromeClient,
                navigator = viewModel.webViewNavigator,
                onCreated = {
                    with(it){
//                        addJavascriptInterface(
//                            NavigateJsObject { e -> viewModel.setSideEffect(e) }, "android")
                        settings.run {
                            javaScriptEnabled = true
                        }
                    }
                },
            )
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
        }
    }
}