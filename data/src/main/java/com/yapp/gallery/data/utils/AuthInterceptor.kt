package com.yapp.gallery.data.utils

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.domain.usecase.auth.GetIdTokenUseCase
import com.yapp.gallery.domain.usecase.auth.SetIdTokenUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val getIdTokenUseCase: GetIdTokenUseCase,
    private val setIdTokenUseCase: SetIdTokenUseCase,
    private val auth: FirebaseAuth
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking { getIdTokenUseCase().last() }
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
        val response = chain.proceed(authRequest)
        // 토큰 만료된 경우 : 401로 옴
        if (token.isEmpty() && response.code == 401){
            auth.currentUser?.getIdToken(true)?.run {
                response.close()
                // 동기 코드로 변경
                val res = Tasks.await(this@run).also {
                    runBlocking { setIdTokenUseCase(it.token) }
                }

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