package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val repository: ExhibitRecordRepository
) {
    operator fun invoke(category: String) = repository.createCategory(category)
}