package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val repository: ExhibitRecordRepository
) {
    operator fun invoke(postId: Long) = repository.deleteRecord(postId)
}