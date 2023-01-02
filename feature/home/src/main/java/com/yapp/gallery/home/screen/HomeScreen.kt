package com.yapp.gallery.home.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.*
import com.yapp.gallery.common.theme.color_background
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.home.R

@Composable
fun HomeScreen(
    navigateToInfo: () -> Unit
){
    // Web Client
    val webViewClient = AccompanistWebViewClient()
    val webChromeClient = AccompanistWebChromeClient()

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
            WebView(state = viewModel.webViewState,
                client = webViewClient,
                chromeClient = webChromeClient,
                onCreated = { webView ->
                    with(webView) {
                        settings.run {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            javaScriptCanOpenWindowsAutomatically = false
                        }
                    }
                }
            )
//            Spacer(modifier = Modifier.height(24.dp))
//            Row() {
//                Button(onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
//                    shape = RoundedCornerShape(70.dp)
//                ) {
//                    Text(text = "전체 기록",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        fontFamily = pretendard,
//                        fontWeight = FontWeight.SemiBold
//                    )
//
//                }
//            }
        }
    }
}