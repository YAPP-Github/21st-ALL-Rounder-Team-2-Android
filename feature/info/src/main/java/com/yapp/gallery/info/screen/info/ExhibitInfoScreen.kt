package com.yapp.gallery.info.screen.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.WebView
import com.yapp.gallery.common.util.WebViewClient.webChromeClient
import com.yapp.gallery.common.util.WebViewClient.webViewClient
import com.yapp.gallery.info.utils.InfoNavigateJsObject
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExhibitInfoScreen(
    navigateToCamera: () -> Unit,
    navigateToGallery: () -> Unit,
    viewModel : ExhibitInfoViewModel = hiltViewModel()
){
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
                        addJavascriptInterface(InfoNavigateJsObject { action, payload ->
                            viewModel.setInfoSideEffect(action, payload) }, "android")
                        settings.run {
                            javaScriptEnabled = true
                        }
                    }
                },
            )
        }
    }
}