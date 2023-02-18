package com.yapp.gallery.camera

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yapp.gallery.camera.databinding.ActivityCameraBinding
import com.yapp.gallery.common.util.onCheckPermissions
import com.yapp.navigator.saver.SaverNavigator
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    @Inject
    lateinit var saverNavigator: SaverNavigator
    private lateinit var binding: ActivityCameraBinding
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val permissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGrant ->
            if (isGrant) {
                binding.composeView.setContent {
                    CameraView(
                        onImageCapture = { startActivity(saverNavigator.intentTo(this, it)) },
                        onDismiss = { finish() },
                        outputDirectory = getFileOutput(),
                        executor = cameraExecutor
                    )
                }
            } else {
                // todo 퍼미션 거절 되었을 시 dialog 노출 후 화면 이탈 필요
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView<ActivityCameraBinding>(this, R.layout.activity_camera)
                .also {
                    it.lifecycleOwner = this
                    it.executePendingBindings()
                }

        onCheckPermissions(
            activity = this,
            permission = Manifest.permission.CAMERA,
            onRequest = { permissionLaunch.launch(Manifest.permission.CAMERA) },
            onGrant = { permissionLaunch.launch(Manifest.permission.CAMERA) },
            onDeny = { permissionLaunch.launch(Manifest.permission.CAMERA) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun getFileOutput(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
}
