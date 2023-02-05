package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class GetTempPostUseCase @Inject constructor(
    private val repository: ExhibitRecordRepository
) {
    operator fun invoke() = repository.getTempPost()
}