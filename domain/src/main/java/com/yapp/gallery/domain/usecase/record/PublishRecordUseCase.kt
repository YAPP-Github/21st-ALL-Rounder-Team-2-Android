package com.yapp.gallery.domain.usecase.record

import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class PublishRecordUseCase @Inject constructor(
    private val exhibitRecordRepository: ExhibitRecordRepository
) {
    operator fun invoke(postId: Long) = exhibitRecordRepository.publishRecord(postId)
}
