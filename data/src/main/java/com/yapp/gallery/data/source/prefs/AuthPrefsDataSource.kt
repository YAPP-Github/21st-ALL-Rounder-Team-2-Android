package com.yapp.gallery.data.source.prefs

import com.yapp.gallery.data.model.IdTokenInfo
import kotlinx.coroutines.flow.Flow

interface AuthPrefsDataSource {
    suspend fun setLoginType(loginType: String)
    suspend fun setIdToken(idToken: String)

    suspend fun getLoginType() : String?

    suspend fun getRefreshedToken() : String
    fun getIdToken() : Flow<String>
    fun getIdTokenExpiredTime() : Flow<String>

    suspend fun deleteLoginInfo()
}