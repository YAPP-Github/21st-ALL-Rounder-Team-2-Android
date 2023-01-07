package com.yapp.gallery.data.repository

import com.yapp.gallery.data.remote.record.ExhibitRecordRemoteDataSource
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class ExhibitRecordRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExhibitRecordRemoteDataSource
) : ExhibitRecordRepository{
    override suspend fun getCategoryList(): List<CategoryItem> {
        return remoteDataSource.getCategoryList()
    }
}