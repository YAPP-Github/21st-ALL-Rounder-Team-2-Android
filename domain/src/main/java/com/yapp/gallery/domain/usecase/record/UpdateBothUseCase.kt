package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class UpdateBothUseCase @Inject constructor(
    private val repository: ExhibitRecordRepository
) {
    operator fun invoke(postId: Long, name: String, categoryId: Long, postDate: String, postLink: String?)
        = repository.updateBoth(postId, name, categoryId, postDate, postLink)
}