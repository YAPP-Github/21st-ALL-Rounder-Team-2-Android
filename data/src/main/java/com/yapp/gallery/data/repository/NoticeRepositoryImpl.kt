package com.yapp.gallery.data.repository

import com.yapp.gallery.data.source.remote.notice.NoticeRemoteDataSource
import com.yapp.gallery.domain.entity.notice.NoticeItem
import com.yapp.gallery.domain.repository.NoticeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val remoteDataSource: NoticeRemoteDataSource
) : NoticeRepository{
    override fun getNoticeList(): Flow<List<NoticeItem>> {
        return remoteDataSource.getNoticeList()
    }
}