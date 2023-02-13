package com.yapp.gallery.data.source.remote.category

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.domain.entity.category.CategoryPost
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryManageRemoteDataSourceImpl @Inject constructor(
    private val artieSerivce: ArtieSerivce,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CategoryManageRemoteDataSource{
    override fun getCategoryPost(
        pageNum: Int, size: Int, categoryId: Long
    ): Flow<CategoryPost> = flow {
        emit(artieSerivce.getCategoryPost(page = pageNum, size = size, id = categoryId))
    }.flowOn(ioDispatcher)
}