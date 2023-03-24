package com.yapp.gallery.domain.usecase.login

import com.yapp.gallery.domain.repository.LoginRepository
import javax.inject.Inject

class PostKakaoLoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(accessToken : String) = repository.kakaoLogin(accessToken)
}