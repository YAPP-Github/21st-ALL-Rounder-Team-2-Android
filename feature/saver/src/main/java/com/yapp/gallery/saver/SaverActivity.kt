package com.yapp.gallery.saver

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.saver.databinding.ActivitySaverBinding

class SaverActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView<ActivitySaverBinding>(this, R.layout.activity_saver)
                .also {
                    it.lifecycleOwner = this
                    it.executePendingBindings()
                }

        val uri = intent.getParcelableExtra("uri") as? Uri
        val uris = intent.getParcelableArrayExtra("uris")

        binding.composeView.setContent {
            if(!uris.isNullOrEmpty()) {
                GalleryTheme {
                    SaverView(onRetryListener = {
                        finish()
                    }, saveToFile = {
                        SaverDialog.show(supportFragmentManager)
                    }, uris= uris.filterIsInstance<Uri>())
                }
            }

            uri?.let {
                GalleryTheme {
                    SaverView(
                        uri = it,
                        onRetryListener = { finish() },
                        saveToFile = {
                            SaverDialog.show(supportFragmentManager)
                        }
                    )
                }
            }
        }
    }
}
