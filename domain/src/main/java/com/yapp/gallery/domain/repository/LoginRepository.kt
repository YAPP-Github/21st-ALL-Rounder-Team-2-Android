package com.yapp.gallery.domain.repository


interface LoginRepository {
    suspend fun tokenLogin(accessToken : String) : String
    suspend fun createUser(idToken: String, firebaseUserId: String) : Long
}