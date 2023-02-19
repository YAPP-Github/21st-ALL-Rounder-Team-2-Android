package com.yapp.gallery.domain.usecase.profile

import com.yapp.gallery.domain.repository.ProfileRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: ProfileRepository
){
    operator fun invoke() = repository.signOut()
}