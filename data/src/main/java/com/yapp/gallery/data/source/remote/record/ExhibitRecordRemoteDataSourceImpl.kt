package com.yapp.gallery.data.source.remote.record

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.data.model.CategoryBody
import com.yapp.gallery.data.model.CreateRecordBody
import com.yapp.gallery.domain.entity.home.CategoryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExhibitRecordRemoteDataSourceImpl @Inject constructor(
    private val artieSerivce: ArtieSerivce,
    @IoDispatcher private val dispatcher : CoroutineDispatcher
) : ExhibitRecordRemoteDataSource {
    override fun getCategoryList()
    : Flow<List<CategoryItem>> = flow {
        emit(artieSerivce.getCategoryList())
    }.flowOn(dispatcher)

    override fun createCategory(category: String)
    : Flow<Long> = flow {
        emit(artieSerivce.createCategory(CategoryBody(category)).id)
    }.flowOn(dispatcher)

    override fun createRecord(
        name: String,
        categoryId: Long,
        postDate: String,
    ): Flow<Long> = flow {
        emit(artieSerivce.createRecord(CreateRecordBody(name, categoryId, postDate)).id)
    }.flowOn(dispatcher)
}