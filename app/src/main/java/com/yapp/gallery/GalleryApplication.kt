package com.yapp.gallery

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import com.yapp.gallery.BuildConfig.KAKAO_NATIVE_APP_KEY
import com.yapp.gallery.login.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GalleryApplication : Application() {
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)

        // idToken 리스너
        auth.addIdTokenListener(FirebaseAuth.IdTokenListener {
            it.currentUser?.run {
                getIdToken(true).addOnSuccessListener { task ->
                    sharedPreferences.edit().putString("idToken", task.token).apply()
                    Log.e("idToken Refresh", task.token.toString())
                }
            }
        })

        // 카카오 SDK init
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
        // 네이버 SDK init
        NaverIdLoginSDK.initialize(this,
            BuildConfig.NAVER_OAUTH_CLIENT_ID,
            BuildConfig.NAVER_OAUTH_CLIENT_SECRET,
            "아르티"
        )
        val keyHash = Utility.getKeyHash(this)
        Log.e("keyHash", keyHash)
    }
}