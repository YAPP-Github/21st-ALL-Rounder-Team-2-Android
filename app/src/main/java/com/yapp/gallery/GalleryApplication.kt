package com.yapp.gallery

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.yapp.gallery.BuildConfig.KAKAO_NATIVE_APP_KEY
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GalleryApplication : Application() {
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)

        auth.addIdTokenListener(FirebaseAuth.IdTokenListener {
            it.currentUser?.run {
                getIdToken(false).addOnSuccessListener {task ->
                    sharedPreferences.edit().putString("idToken", task.token).apply()
                    Log.e("idToken", task.token.toString())
                }
            }
        })

        // 카카오 SDK init
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY)
        val keyHash = Utility.getKeyHash(this)
        Log.e("keyHash", keyHash)
    }
}