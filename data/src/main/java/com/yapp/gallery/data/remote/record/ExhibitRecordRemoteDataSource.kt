package com.yapp.gallery.data.remote.record

import com.yapp.gallery.domain.entity.home.CategoryItem

interface ExhibitRecordRemoteDataSource {
    suspend fun getCategoryList() : List<CategoryItem>
    suspend fun createCategory(category: String) : Long
    suspend fun createRecord(name: String, categoryId: Long, postDate: String) : Long
}