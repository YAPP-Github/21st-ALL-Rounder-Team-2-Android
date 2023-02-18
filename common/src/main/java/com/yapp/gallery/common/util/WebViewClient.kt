package com.yapp.gallery.common.util

import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient

object WebViewClient {
    val webViewClient = AccompanistWebViewClient()
    val webChromeClient = AccompanistWebChromeClient()
}