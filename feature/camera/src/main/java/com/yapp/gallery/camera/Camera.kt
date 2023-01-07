package com.yapp.gallery.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraView(
    outputDirectory: File,
    onImageCapture: (Uri) -> Unit,
    executor: Executor,
    onDismiss: () -> Unit
) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val preview = Preview.Builder().build()
    val previewView: PreviewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    LaunchedEffect(key1 = cameraSelector) {
        val cameraProvider = context.getCameraProvider()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycle,
            cameraSelector,
            preview,
            imageCapture
        )
        cameraProvider.bindToLifecycle(
            lifecycle,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter) {

        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .alpha(0.7f)
                .background(Color.Black)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 27.dp),
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
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
                        outputDirectory,
                        imageCapture,
                        executor,
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

private fun moveToResultScreen(
    fileName: String,
    outputDirectory: File,
    imageCapture: ImageCapture,
    executor: Executor,
    onImageCapture: (Uri) -> Unit
) {
    val file = File(
        outputDirectory,
        SimpleDateFormat(fileName, Locale.KOREAN).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {}

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(file)
            onImageCapture(savedUri)
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }
