package com.yapp.gallery.saver

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yapp.navigator.saver.SaverNavigator
import javax.inject.Inject

class SaverNavigatorImpl @Inject constructor(): SaverNavigator {
    override fun intentTo(context: Context, uri: Uri): Intent {
        return Intent(context, SaverActivity::class.java).putExtra("uri", uri)
    }

    override fun navigate(context: Context): Intent {
        return Intent(context, SaverActivity::class.java)
    }
}
