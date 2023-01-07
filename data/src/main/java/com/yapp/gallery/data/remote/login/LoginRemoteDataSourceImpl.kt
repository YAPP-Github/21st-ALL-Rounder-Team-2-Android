package com.yapp.gallery.data.remote.login

import com.yapp.gallery.data.api.ArtieLoginSerivce
import com.yapp.gallery.data.api.ArtieTokenService
import com.yapp.gallery.data.body.TokenLoginBody
import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.login.FirebaseToken
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val artieLoginService: ArtieTokenService,
    private val artieService: ArtieLoginSerivce
) : LoginRemoteDataSource{
    override suspend fun tokenLogin(accessToken: String): FirebaseToken {
        return artieLoginService.tokenLogin(TokenLoginBody(accessToken))
    }

    override suspend fun createUser(idToken: String, firebaseUserId: String): CreateUserResponse {
        return artieService.createUser("Bearer $idToken", firebaseUserId)
    }
}