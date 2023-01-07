package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class CreateRecordUseCase @Inject constructor(
    private val repository: ExhibitRecordRepository
) {
    suspend operator fun invoke(name: String, categoryId: Long, postDate: String)
        = repository.createRecord(name, categoryId, postDate)
}