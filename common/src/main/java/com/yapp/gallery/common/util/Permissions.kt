package com.yapp.gallery.common.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.onCheckPermissions(
    activity: Activity, permission: String,
    onGrant: () -> Unit, onRequest: () -> Unit,
    onDeny: () -> Unit
) {
    when {
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED -> {
            onRequest.invoke()
        }

        onRequestPermission(activity, permission) -> {
            onGrant.invoke()
        }

        else -> onDeny.invoke()
    }
}


private fun onRequestPermission(activity: Activity, permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

