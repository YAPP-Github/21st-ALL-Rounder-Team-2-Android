package com.yapp.gallery.data.source.remote.record

import com.yapp.gallery.domain.entity.home.CategoryItem
import kotlinx.coroutines.flow.Flow

interface ExhibitRecordRemoteDataSource {
    fun getCategoryList() : Flow<List<CategoryItem>>
    fun createCategory(category: String) : Flow<Long>
    fun createRecord(name: String, categoryId: Long, postDate: String, attachedLink: String?) : Flow<Long>
    fun updateRecord(postId: Long, name: String, categoryId: Long, postDate: String, attachedLink: String?) : Flow<Boolean>
    fun deleteRecord(postId: Long) : Flow<Boolean>
    fun publishRecord(postId: Long): Flow<Boolean>
}