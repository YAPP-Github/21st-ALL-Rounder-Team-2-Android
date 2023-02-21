package com.yapp.gallery.common.util.webview

import android.util.Log
import android.webkit.JavascriptInterface
import org.json.JSONObject

class NavigateJsObject(
    private val setSideEffect : (String, String?) -> Unit
) {
    @JavascriptInterface
    fun postMessage(data: String) : Boolean{
        val json = JSONObject(data)
        Log.e("info Js", data)
        var payload : String? = null
        val action = json.getString("action")
        if (json.has("payload")){
            payload = json.getString("payload")
        }
        setSideEffect(action, payload)
        return true
    }
}