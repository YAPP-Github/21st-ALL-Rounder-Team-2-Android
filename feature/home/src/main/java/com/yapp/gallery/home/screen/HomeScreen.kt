package com.yapp.gallery.home.screen

import android.app.Activity
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.home.screen.jsInterface.NavigateJsObject

@Composable
fun HomeScreen(
    navigateToInfo: () -> Unit
){
    val viewModel = hiltViewModel<HomeViewModel>()
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToInfo,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                shape = CircleShape,
                // 기본 마진 16dp 인듯
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 48.dp)
                    .size(72.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            AndroidView(factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
//                    loadUrl("file:///android_asset/example.html")
//                    addJavascriptInterface(NavigateJsObject(it as Activity, navigateToInfo), "android")
                    loadUrl("https://21st-all-rounder-team-2-web-bobeenlee.vercel.app/home")
                    loadUrl("javascript: window.onload=function(){ window.android.postMessage('testing!'); }")
                    addJavascriptInterface(NavigateJsObject(it as Activity, navigateToInfo), "android")
                }
            })
        }
    }

}

