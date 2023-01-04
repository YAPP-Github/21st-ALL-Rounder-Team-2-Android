package com.yapp.gallery.data.remote.login

import com.yapp.gallery.data.api.ArtieLoginService
import com.yapp.gallery.domain.entity.login.FirebaseToken
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val artieLoginService: ArtieLoginService
) : LoginRemoteDataSource{
    override suspend fun kakaoLogin(accessToken: String): FirebaseToken {
        return artieLoginService.kakaoLogin(accessToken)
    }
}