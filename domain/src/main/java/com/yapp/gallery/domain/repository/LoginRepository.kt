package com.yapp.gallery.domain.repository


interface LoginRepository {
    suspend fun kakaoLogin(accessToken : String) : String
}