package com.yapp.navigator.saver

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yapp.gallery.core.Navigator

interface SaverNavigator : Navigator {
    fun intentTo(context: Context, uri: Uri): Intent
}
