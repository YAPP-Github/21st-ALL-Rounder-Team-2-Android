package com.yapp.gallery.common.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.onCheckPermissions(activity: Activity, permission: String, onGrant: () -> Unit) {
    when {
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED -> Unit

        onRequestPermission(activity, permission) -> {
            Log.i("yapp", "Show camera permissions dialog")
        }

        else -> onGrant()
    }
}


private fun onRequestPermission(activity: Activity, permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

