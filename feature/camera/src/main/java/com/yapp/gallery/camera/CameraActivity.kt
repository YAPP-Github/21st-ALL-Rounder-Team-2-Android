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
import javax.inject.Inject

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    @Inject
    lateinit var saverNavigator: SaverNavigator
    private lateinit var binding: ActivityCameraBinding

    private val permissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGrant ->
            if (isGrant) {
                binding.composeView.setContent {
                    CameraView(
                        onImageCapture = { startActivity(saverNavigator.intentTo(this, it)) },
                        onDismiss = { finish() }
                    )
                }
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

        onCheckPermissions(activity = this, permission = Manifest.permission.CAMERA, onGrant = {
            permissionLaunch.launch(Manifest.permission.CAMERA)
        })
    }

}
