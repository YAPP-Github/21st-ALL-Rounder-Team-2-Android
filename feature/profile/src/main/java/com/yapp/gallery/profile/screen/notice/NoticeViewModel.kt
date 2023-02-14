package com.yapp.gallery.profile.screen.notice

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.yapp.gallery.domain.entity.notice.NoticeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(

) : ViewModel() {
    private var _noticeList = mutableStateListOf<NoticeItem>()
    val noticeList : List<NoticeItem>
        get() = _noticeList

    init {
        // Todo : 임시 공지사항 리스트
        _noticeList = mutableStateListOf(
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ"),
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ"),
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ"),
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ"),
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ"),
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ"),
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ"),
            NoticeItem("2023-02-04", 1, "아우라ㅜ알우ㅏ루ㅏ우라ㅜ알아라아라아루ㅏ우라ㅜ아ㅜ라우ㅏ루ㅏㅇ")
        )
    }


}