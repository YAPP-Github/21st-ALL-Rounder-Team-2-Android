package com.yapp.gallery.home.utils

 import android.util.Log
 import android.webkit.JavascriptInterface
 import org.json.JSONObject

class NavigateJsObject(
    private val setSideEffect : (String) -> Unit
) {
    @JavascriptInterface
    fun postMessage(data: String) : Boolean{
        val json = JSONObject(data)
        Log.e("message", data)
        val action = json.getString("action")
        setSideEffect(action)
        return true
    }
}