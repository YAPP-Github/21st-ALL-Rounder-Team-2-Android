<<<<<<< HEAD
<<<<<<< HEAD:feature/info/src/main/java/com/yapp/gallery/info/screen/edit/ExhibitEditState.kt
package com.yapp.gallery.info.screen.edit
=======
package com.yapp.gallery.home.screen.edit
>>>>>>> 11ab159 ([ Feature ] : 전시 정보 삭제 및 업데이트 연동):feature/home/src/main/java/com/yapp/gallery/home/screen/edit/ExhibitEditState.kt
=======
package com.yapp.gallery.info.screen.edit
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)

import com.yapp.gallery.common.model.UiText

sealed class ExhibitEditState {
    object Initial : ExhibitEditState()
    object Delete : ExhibitEditState()
    object Update : ExhibitEditState()
    data class Error(val message: UiText) : ExhibitEditState()
}