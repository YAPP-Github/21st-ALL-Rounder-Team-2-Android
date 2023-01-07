package com.yapp.gallery

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.yapp.gallery.BuildConfig.KAKAO_NATIVE_APP_KEY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)

        // 카카오 SDK init
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
        val keyHash = Utility.getKeyHash(this)
        Log.e("keyHash", keyHash)
    }
}