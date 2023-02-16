package com.yapp.gallery.data.source.remote.notice

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.domain.entity.notice.NoticeItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoticeRemoteDataSourceImpl @Inject constructor(
    private val artieService: ArtieSerivce,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NoticeRemoteDataSource {
    override fun getNoticeList(): Flow<List<NoticeItem>> = flow {
        emit(artieService.getNoticeList())
    }.flowOn(ioDispatcher)
}