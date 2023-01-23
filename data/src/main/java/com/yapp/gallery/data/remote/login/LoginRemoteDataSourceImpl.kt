package com.yapp.gallery.data.remote.login

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.api.ArtieTokenService
import com.yapp.gallery.data.model.TokenLoginBody
import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.login.FirebaseToken
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val artieLoginService: ArtieTokenService,
    private val artieService: ArtieSerivce
) : LoginRemoteDataSource{
    override suspend fun tokenLogin(accessToken: String): FirebaseToken {
        return artieLoginService.tokenLogin(TokenLoginBody(accessToken))
    }

    override suspend fun createUser(firebaseUserId: String): CreateUserResponse {
        return artieService.createUser(firebaseUserId)
    }
}