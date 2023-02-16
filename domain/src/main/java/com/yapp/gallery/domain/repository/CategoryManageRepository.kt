package com.yapp.gallery.domain.repository

import com.yapp.gallery.domain.entity.category.CategoryPost
import com.yapp.gallery.domain.entity.home.CategoryItem
import kotlinx.coroutines.flow.Flow

interface CategoryManageRepository {
    fun getCategoryPost(pageNum : Int, size : Int,  categoryId: Long) : Flow<CategoryPost>
    fun editCategory(categoryId: Long, editedName: String) : Flow<Boolean>
    fun deleteCategory(categoryId: Long) : Flow<Boolean>
    fun editCategorySequence(categoryList: List<CategoryItem>) : Flow<Boolean>
}