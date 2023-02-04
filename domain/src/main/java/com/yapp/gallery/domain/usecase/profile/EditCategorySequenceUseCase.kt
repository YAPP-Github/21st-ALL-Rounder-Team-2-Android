package com.yapp.gallery.domain.usecase.profile

import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.repository.ProfileRepository
import javax.inject.Inject

class EditCategorySequenceUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(categoryList : List<CategoryItem>) = repository.editCategorySequence(categoryList)
}