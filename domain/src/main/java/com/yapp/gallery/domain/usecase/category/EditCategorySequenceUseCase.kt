package com.yapp.gallery.domain.usecase.category

import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.repository.CategoryManageRepository
import javax.inject.Inject

class EditCategorySequenceUseCase @Inject constructor(
    private val repository: CategoryManageRepository
) {
    operator fun invoke(categoryList : List<CategoryItem>) = repository.editCategorySequence(categoryList)
}