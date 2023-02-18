package com.yapp.gallery.domain.usecase.edit

import com.yapp.gallery.domain.repository.ExhibitEditRepository
import javax.inject.Inject

class UpdateRemotePostUseCase @Inject constructor(
    private val repository: ExhibitEditRepository
) {
    operator fun invoke(postId: Long, name: String, categoryId: Long, postDate: String, postLink: String?)
        = repository.updateRemotePost(postId, name, categoryId, postDate, postLink)
}