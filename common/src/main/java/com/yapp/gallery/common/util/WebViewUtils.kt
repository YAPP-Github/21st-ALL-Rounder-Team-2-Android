package com.yapp.gallery.common.util

import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

object WebViewUtils {
    val webViewClient = WebViewClient()
    val webChromeClient = WebChromeClient()
    val cookieManager: CookieManager = CookieManager.getInstance()
}