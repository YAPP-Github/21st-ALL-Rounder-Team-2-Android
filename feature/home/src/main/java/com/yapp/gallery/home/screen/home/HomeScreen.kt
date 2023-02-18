package com.yapp.gallery.home.screen.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.yapp.gallery.home.utils.NavigateJsObject
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navigateToRecord: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToEdit: () -> Unit
){
    val viewModel = hiltViewModel<HomeViewModel>()

    val webViewClient = AccompanistWebViewClient()
    val webChromeClient = AccompanistWebChromeClient()

    LaunchedEffect(viewModel.homeSideEffect){
        viewModel.homeSideEffect.collect {
            when(it){
                "NAVIGATE_TO_EDIT" -> navigateToRecord()
                "NAVIGATE_TO_MY" -> navigateToProfile()
                "NAVIGATE_TO_CALENDAR" -> navigateToCalendar()
                // Todo : 임시
                else -> navigateToEdit()
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
                            javaScriptEnabled = true
                        }
                    }
                },
            )
        }
    }

}

