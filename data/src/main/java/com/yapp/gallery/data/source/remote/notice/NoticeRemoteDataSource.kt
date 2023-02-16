package com.yapp.gallery.data.source.remote.notice

import com.yapp.gallery.domain.entity.notice.NoticeItem
import kotlinx.coroutines.flow.Flow

interface NoticeRemoteDataSource {
    fun getNoticeList(): Flow<List<NoticeItem>>
}