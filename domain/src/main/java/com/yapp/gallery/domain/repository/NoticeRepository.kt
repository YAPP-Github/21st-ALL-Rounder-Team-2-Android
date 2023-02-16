package com.yapp.gallery.domain.repository

import com.yapp.gallery.domain.entity.notice.NoticeItem
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNoticeList() : Flow<List<NoticeItem>>
}