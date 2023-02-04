package com.yapp.gallery.home.screen.record

import com.yapp.gallery.domain.entity.home.TempPostInfo

sealed class ExhibitRecordState{
    // 초기 상태
    object Initial : ExhibitRecordState()
    // 사용자 응답 기다리는 중
    data class Response(val tempPostInfo: TempPostInfo) : ExhibitRecordState()
    // 이어서 하기
    data class Continuous(val tempPostInfo: TempPostInfo) : ExhibitRecordState()
    // 이어서 하지 않거나 기본 상태
    // 해당 상태에서 전시 기록할 시 서버로 요청 보내야 함
    object Normal : ExhibitRecordState()
}
