package com.yapp.gallery.home.screen.jsInterface

 import android.app.Activity
 import android.webkit.JavascriptInterface
 import android.widget.Toast

class NavigateJsObject(
    private val context: Activity,
    private val navigateToRecord : () -> Unit
) {
    @JavascriptInterface
    fun postMessage(json: String) : Boolean{
        Toast.makeText(context, json, Toast.LENGTH_SHORT).show()
        context.runOnUiThread {
            navigateToRecord()
        }
        return true
    }
}