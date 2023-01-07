package com.yapp.gallery.domain.repository

import com.yapp.gallery.domain.entity.home.CategoryItem

interface ExhibitRecordRepository {
    suspend fun getCategoryList() : List<CategoryItem>
    suspend fun createCategory(category: String) : Long
    suspend fun createRecord(name: String, categoryId: Long, postDate: String) : Long
}