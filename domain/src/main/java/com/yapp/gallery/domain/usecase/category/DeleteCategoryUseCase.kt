package com.yapp.gallery.domain.usecase.category

import com.yapp.gallery.domain.repository.CategoryManageRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoryManageRepository
) {
    operator fun invoke(categoryId : Long) = repository.deleteCategory(categoryId)
}