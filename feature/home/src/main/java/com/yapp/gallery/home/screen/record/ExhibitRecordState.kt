package com.yapp.gallery.home.screen.record

import com.yapp.gallery.domain.entity.home.TempPostInfo

sealed class ExhibitRecordState{
    // 초기 상태
    object Initial : ExhibitRecordState()
    // 사용자 응답 기다리는 중
    data class Response(val tempPostInfo: TempPostInfo) : ExhibitRecordState()
    // 이어서 하기
    data class Continuous(val tempPostInfo: TempPostInfo) : ExhibitRecordState()
    // 삭제한 상태
    data class Delete(val tempPostInfo: TempPostInfo) : ExhibitRecordState()
    // 기본 상태
    // 해당 상태에서 전시 기록할 시 서버로 요청 보내야 함
    object Normal : ExhibitRecordState()
    interface Create {
        val postId: Long
    }
    // 이미 생성된 적 있는 상태
    data class CreatedCamera(override val postId: Long) : ExhibitRecordState(), Create
    data class CreatedAlbum(override val postId: Long) : ExhibitRecordState(), Create
}