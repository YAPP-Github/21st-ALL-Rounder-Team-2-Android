package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    private val repository: ExhibitRecordRepository
) {
    suspend operator fun invoke() = repository.getCategoryList()
}