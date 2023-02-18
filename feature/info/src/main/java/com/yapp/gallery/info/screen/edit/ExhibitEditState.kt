package com.yapp.gallery.info.screen.edit

import com.yapp.gallery.common.model.UiText

sealed class ExhibitEditState {
    object Initial : ExhibitEditState()
    object Delete : ExhibitEditState()
    object Update : ExhibitEditState()
    data class Error(val message: UiText) : ExhibitEditState()
}