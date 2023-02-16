package com.yapp.gallery.domain.usecase.category

import com.yapp.gallery.domain.repository.CategoryManageRepository
import javax.inject.Inject

class EditCategoryUseCase @Inject constructor(
    private val repository: CategoryManageRepository
) {
    operator fun invoke(categoryId: Long, editedName: String) = repository.editCategory(categoryId, editedName)
}