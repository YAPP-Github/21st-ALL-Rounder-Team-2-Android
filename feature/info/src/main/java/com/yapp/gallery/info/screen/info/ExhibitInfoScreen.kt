package com.yapp.gallery.info.screen.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.WebView
import com.yapp.gallery.common.util.WebViewClient.webChromeClient
import com.yapp.gallery.common.util.WebViewClient.webViewClient

@Composable
fun ExhibitInfoScreen(
    viewModel : ExhibitInfoViewModel = hiltViewModel()
){
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
//                        addJavascriptInterface(
//                            NavigateJsObject { e -> viewModel.setSideEffect(e) }, "android")
                        settings.run {
                            javaScriptEnabled = true
                        }
                    }
                },
            )
        }
    }
}