package com.yapp.gallery.domain.usecase.notice

import com.yapp.gallery.domain.repository.NoticeRepository
import javax.inject.Inject

class GetNoticeListUseCase @Inject constructor(
    private val repository: NoticeRepository
) {
    operator fun invoke() = repository.getNoticeList()
}