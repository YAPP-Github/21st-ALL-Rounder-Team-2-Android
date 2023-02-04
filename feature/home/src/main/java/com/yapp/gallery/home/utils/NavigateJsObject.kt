package com.yapp.gallery.home.utils

 import android.app.Activity
 import android.util.Log
 import android.webkit.JavascriptInterface
 import android.widget.Toast
 import org.json.JSONObject

class NavigateJsObject(
    private val context: Activity,
    private val navigateToRecord : () -> Unit,
    private val navigateToProfile : () -> Unit,
) {
    @JavascriptInterface
    fun postMessage(data: String) : Boolean{
//        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
        val json = JSONObject(data)
        val action = json.getString("action")
        context.runOnUiThread {
            when(action){
                "NAVIGATE_TO_EDIT" -> navigateToRecord()
                "NAVIGATE_TO_MY" -> navigateToProfile()
                else -> {
                    // TODO : 나머지 로직
                }
            }
        }
        return true
    }
}