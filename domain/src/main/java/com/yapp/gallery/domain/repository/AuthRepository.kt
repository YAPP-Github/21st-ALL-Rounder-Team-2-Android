package com.yapp.gallery.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun setLoginType(loginType: String) : Flow<Unit>
    fun setIdToken(idToken: String) : Flow<Unit>
    fun getLoginType() : Flow<String>
    suspend fun getIdToken() : String
    fun getRefreshedToken() : Flow<String>

    fun deleteLoginInfo() : Flow<Unit>
}