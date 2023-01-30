package com.yapp.gallery.data.utils

import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val auth: FirebaseAuth
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = sharedPreferences.getString("idToken", "")
        val authRequest = if (token.isNullOrEmpty()){
            originalRequest.newBuilder()
                .build()
        } else {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build().also {
                    Log.e("okhttpHeader", token)
                }
        }
        val response = chain.proceed(authRequest)
        // 토큰 만료된 경우 : 401로 옴
        if (!token.isNullOrEmpty() && response.code == 401){
            auth.currentUser?.getIdToken(true)?.run {
                response.close()
                // 동기 코드로 변경
                val res = Tasks.await(this)
                sharedPreferences.edit().putString("idToken", res.token).apply()
                val refreshedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${res.token}")
                    .build().also {
                        Log.e("refreshedOkhttpHeader", res.token.toString())
                    }
                return chain.proceed(refreshedRequest)
            }
        }
        return response
    }
}