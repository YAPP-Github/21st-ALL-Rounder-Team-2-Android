package com.yapp.gallery.common.util

import android.webkit.CookieManager
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient

object WebViewUtils {
    val webViewClient = AccompanistWebViewClient()
    val webChromeClient = AccompanistWebChromeClient()
    val cookieManager: CookieManager = CookieManager.getInstance()
}