package com.yapp.gallery.domain.usecase.edit

import com.yapp.gallery.domain.repository.ExhibitEditRepository
import javax.inject.Inject

class DeleteRemotePostUseCase @Inject constructor(
    private val repository: ExhibitEditRepository
) {
    operator fun invoke(postId: Long) = repository.deleteRemotePost(postId)
}