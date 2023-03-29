package com.yapp.gallery.domain.usecase.auth

import com.yapp.gallery.domain.repository.AuthRepository
import javax.inject.Inject

class GetRefreshedTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.getRefreshedToken()
}