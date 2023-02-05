package com.yapp.gallery.data.source.remote.login

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.api.login.ArtieKakaoService
import com.yapp.gallery.data.api.login.ArtieNaverService
import com.yapp.gallery.data.model.TokenLoginBody
import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.login.FirebaseToken
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val artieKakaoService: ArtieKakaoService,
    private val artieNaverService: ArtieNaverService,
    private val artieService: ArtieSerivce
) : LoginRemoteDataSource {
    override suspend fun kakaoLogin(accessToken: String): FirebaseToken {
        return artieKakaoService.tokenLogin(TokenLoginBody(accessToken))
    }

    override suspend fun naverLogin(accessToken: String): FirebaseToken {
        return artieNaverService.tokenLogin(TokenLoginBody(accessToken))
    }

    override suspend fun createUser(firebaseUserId: String): CreateUserResponse {
        return artieService.createUser(firebaseUserId)
    }
}