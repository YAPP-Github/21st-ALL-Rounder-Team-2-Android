package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class DeleteBothUseCase @Inject constructor(
    private val repository: ExhibitRecordRepository
) {
    operator fun invoke() = repository.deleteBoth()
}