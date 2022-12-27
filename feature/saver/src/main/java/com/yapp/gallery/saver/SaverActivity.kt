package com.yapp.gallery.saver

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yapp.gallery.saver.databinding.ActivitySaverBinding

class SaverActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySaverBinding>(this, R.layout.activity_saver)
            .also {
                it.lifecycleOwner = this
                it.executePendingBindings()
            }

        val uri = intent.getParcelableExtra("uri") as? Uri

        binding.composeView.setContent {
            SaverView(uri = uri!!)
        }
    }
}
