package com.yapp.gallery.domain.repository

import androidx.paging.PagingData
import com.yapp.gallery.domain.entity.category.CategoryPost
import com.yapp.gallery.domain.entity.category.PostContent
import com.yapp.gallery.domain.entity.home.CategoryItem
import kotlinx.coroutines.flow.Flow

interface CategoryManageRepository {
    fun getCategoryPost(categoryId: Long) : Flow<PagingData<PostContent>>
    fun editCategory(categoryId: Long, editedName: String) : Flow<Boolean>
    fun deleteCategory(categoryId: Long) : Flow<Boolean>
    fun editCategorySequence(categoryList: List<CategoryItem>) : Flow<Boolean>
}