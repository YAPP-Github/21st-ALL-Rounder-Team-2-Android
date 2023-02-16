package com.yapp.gallery.domain.usecase.category

import com.yapp.gallery.domain.repository.CategoryManageRepository
import javax.inject.Inject

class GetCategoryPostUseCase @Inject constructor(
    private val repository: CategoryManageRepository
) {
    operator fun invoke(pageNum : Int = 0, size: Int = 20, postId: Long)
        = repository.getCategoryPost(pageNum, size, postId)
}