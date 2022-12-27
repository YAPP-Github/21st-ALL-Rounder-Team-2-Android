package com.yapp.gallery.saver

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import coil.compose.rememberAsyncImagePainter

@Composable
fun SaverView(uri: Uri) {
    Image(painter = rememberAsyncImagePainter(uri), contentDescription = null)
}
