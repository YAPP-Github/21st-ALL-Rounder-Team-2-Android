package com.yapp.gallery.data.remote.record

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.domain.entity.home.CategoryItem
import javax.inject.Inject

class ExhibitRecordRemoteDataSourceImpl @Inject constructor(
    private val artieSerivce: ArtieSerivce
) : ExhibitRecordRemoteDataSource {
    override suspend fun getCategoryList(): List<CategoryItem>
        = artieSerivce.getCategoryList()
}