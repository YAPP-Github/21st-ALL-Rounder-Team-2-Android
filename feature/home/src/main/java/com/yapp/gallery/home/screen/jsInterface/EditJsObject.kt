package com.yapp.gallery.home.screen.jsInterface

 import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class EditJsObject(private val context: Context) {
    @JavascriptInterface
    fun postMessage(json: String) : Boolean{
        Toast.makeText(context, json, Toast.LENGTH_SHORT).show()
        return true
    }
}