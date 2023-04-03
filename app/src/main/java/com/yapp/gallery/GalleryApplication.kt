package com.yapp.gallery

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import com.yapp.gallery.BuildConfig.KAKAO_NATIVE_APP_KEY
import com.yapp.gallery.login.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)

        // 카카오 SDK init
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
        // 네이버 SDK init
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_OAUTH_CLIENT_ID,
            BuildConfig.NAVER_OAUTH_CLIENT_SECRET,
            "아르티"
        )

        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        val keyHash = Utility.getKeyHash(this)
        Timber.tag("keyHash").e(keyHash)
    }
}