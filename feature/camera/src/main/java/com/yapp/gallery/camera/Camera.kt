package com.yapp.gallery.camera

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material.icons.twotone.ChangeCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraView(
    onImageCapture: (Uri) -> Unit,
    onDismiss:() -> Unit
) {
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val preview = Preview.Builder().build()
    val previewView: PreviewView = remember { PreviewView(context) }
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    LaunchedEffect(key1 = cameraSelector) {

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycle, cameraSelector, preview)
        }, ContextCompat.getMainExecutor(context))

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter) {

        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        IconButton(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(start = 25.dp, top = 27.dp),
            onClick = onDismiss
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier
                    .size(74.dp)
                    .weight(1f),
                onClick = {
                    moveToResultScreen(
                        fileName = "yyyy-MM-dd-HH-mm-ss-SSS",
                        onImageCapture
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Sharp.Lens,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }

            IconButton(
                modifier = Modifier
                    .size(53.dp)
                    .weight(1f),
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.TwoTone.ChangeCircle,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }

}

private fun moveToResultScreen(fileName: String, onImageCapture: (Uri) -> Unit) {
    val file = File(SimpleDateFormat(fileName, Locale.KOREAN).format(System.currentTimeMillis()) + ".jpg")

    onImageCapture(Uri.fromFile(file))
}
