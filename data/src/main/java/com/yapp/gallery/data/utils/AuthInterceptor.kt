package com.yapp.gallery.data.utils

import android.util.Log
import com.yapp.gallery.domain.usecase.auth.GetIdTokenUseCase
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val getIdTokenUseCase: GetIdTokenUseCase,
    private val getRefreshedTokenUseCase: GetRefreshedTokenUseCase
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking { getIdTokenUseCase() }
        Log.e("getIdTokenUseCase", token.toString())
        val authRequest = if (token.isEmpty()){
            originalRequest.newBuilder()
                .build()
        } else {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build().also {
                    Log.e("okhttpHeader", token)
                }
        }

        var response = chain.proceed(authRequest)
        // 토큰 만료된 경우 : 401로 옴
        if (token.isEmpty() || response.code == 401){
            val refreshedToken = runBlocking { getRefreshedTokenUseCase() }
            response.close()
            val refreshedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $refreshedToken")
                .build().also {
                    Log.e("refreshedOkhttpHeader", refreshedToken)
                }
            response = chain.proceed(refreshedRequest)
        }
        return response
    }
}