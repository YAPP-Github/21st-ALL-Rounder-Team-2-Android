package com.yapp.gallery.saver

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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

        binding.composeView.setContent {
            uri?.let {
                SaverView(
                    uri = it,
                    saveToFile = {
                        /**
                         * 해당 함수 호출부에서 Dialog를 띄워주시면 됩니다 !
                         * finishAffinity -> https://velog.io/@windsekirun/Android-Exit-Applicaction-with-finishAffinity
                         *
                         * finishAffinity 는 동일 테스크에 존재하는 액티비티를 닫아버리는거라 CameraActivity 는 다른 테스트로
                         * 호출 하셔야합니다 ~
                         **/
                        finishAffinity()
                    }
                )
            }
        }
    }
}
