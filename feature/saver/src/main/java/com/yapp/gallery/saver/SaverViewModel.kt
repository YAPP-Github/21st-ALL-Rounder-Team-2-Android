package com.yapp.gallery.saver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.domain.usecase.record.DeleteTempPostUseCase
import com.yapp.gallery.domain.usecase.record.PublishRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaverViewModel @Inject constructor(
    private val publishRecordUseCase: PublishRecordUseCase,
    private val deleteTempPostUseCase: DeleteTempPostUseCase
): ViewModel() {
    private val _publishRecord = MutableSharedFlow<Unit>()
    val publishRecord: SharedFlow<Unit>
        get() = _publishRecord.asSharedFlow()

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit>
        get() = _finish.asSharedFlow()

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

    fun onPublishRecord(postId: Long) {
        viewModelScope.launch {
            publishRecordUseCase.invoke(postId).flatMapConcat {
                deleteTempPostUseCase.invoke()
            }.collectLatest { _publishRecord.emit(Unit) }
        }
    }
}
