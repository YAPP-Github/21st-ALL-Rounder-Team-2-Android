package com.yapp.gallery.saver

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SaverViewModel @Inject constructor(

): ViewModel() {
    private val _exhibit = MutableStateFlow(Exhibit.Init)
    val exhibit: StateFlow<Exhibit>
        get() = _exhibit.asStateFlow()


    data class Exhibit(
        val name: String,
        val title: String
    ) {
        companion object {
            internal val Init = Exhibit(
                name = "",
                title = ""
            )
        }
    }

    fun update(reduce: Exhibit.() -> Exhibit) {
        val newState = _exhibit.value.reduce()

        _exhibit.update { newState }
    }
}
