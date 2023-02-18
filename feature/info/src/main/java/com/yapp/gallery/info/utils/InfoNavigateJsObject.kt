package com.yapp.gallery.info.utils

import android.webkit.JavascriptInterface
import org.json.JSONObject

class InfoNavigateJsObject(
    private val setSideEffect : (String, String) -> Unit
) {
    @JavascriptInterface
    fun postMessage(data: String) : Boolean{
        val json = JSONObject(data)
        val action = json.getString("action")
        val payload = json.getString("payload") ?: ""
        setSideEffect(action, payload)
        return true
    }
}