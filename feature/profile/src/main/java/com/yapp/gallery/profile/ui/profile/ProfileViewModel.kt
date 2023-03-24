package com.yapp.gallery.profile.ui.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.yapp.gallery.common.base.BaseStateViewModel
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.usecase.auth.DeleteLoginInfoUseCase
import com.yapp.gallery.domain.usecase.auth.GetLoginTypeUseCase
import com.yapp.gallery.domain.usecase.profile.GetUserUseCase
import com.yapp.gallery.domain.usecase.record.DeleteBothUseCase
import com.yapp.gallery.profile.R
import com.yapp.gallery.profile.ui.profile.ProfileContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    private val kakaoClient: UserApiClient,
    private val getUserUseCase: GetUserUseCase,
    private val getLoginTypeUseCase: GetLoginTypeUseCase,
    private val deleteBothUseCase: DeleteBothUseCase,
    private val deleteLoginInfoUseCase: DeleteLoginInfoUseCase
) : BaseStateViewModel<ProfileState, ProfileEvent, ProfileSideEffect>() {
    override val _viewState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Initial)

    private var _errorChannel = Channel<UiText>()
    val errors = _errorChannel.receiveAsFlow()

    init {
        getUser()
    }

    private fun getUser(){
        getUserUseCase()
            .catch {
                setViewState(ProfileState.Error(it.message))
                _errorChannel.send(UiText.StringResource(R.string.profile_load_error))
            }
            .onEach {
                setViewState(ProfileState.Success(it))
            }
            .launchIn(viewModelScope)
    }

    private fun removeInfo(){
        viewModelScope.launch {
            deleteBothUseCase()
                .catch {
                    Timber.tag("removeProfile").e(it.message.toString()) }
                .collect()
            logout()
        }
    }

    private suspend fun logout(){
        getLoginTypeUseCase().first{
            when(it){
                "kakao" -> {
                    kakaoClient.logout {
                        auth.signOut()
                    }
                }
                "naver" -> {
                    NaverIdLoginSDK.logout().also {
                        auth.signOut()
                    }
                }
                else -> {
                    googleSignInClient.signOut().addOnCompleteListener {
                        auth.signOut()
                    }
                }
            }
            deleteLoginInfoUseCase()
                .catch {
                    Timber.e(it.message.toString()) }
                .collect()

            sendSideEffect(ProfileSideEffect.NavigateToLogin)
            return@first true
        }
    }

    override fun handleEvents(event: ProfileEvent) {
        when(event){
            is ProfileEvent.OnManageClick -> {
                sendSideEffect(ProfileSideEffect.NavigateToManage)
            }
            is ProfileEvent.OnNicknameClick -> {
                sendSideEffect(ProfileSideEffect.NavigateToNickname)
            }
            is ProfileEvent.OnLegacyClick -> {
                sendSideEffect(ProfileSideEffect.NavigateToLegacy)
            }
            is ProfileEvent.OnNoticeClick -> {
                sendSideEffect(ProfileSideEffect.NavigateToNotice)
            }
            is ProfileEvent.OnSignOutClick -> {
                sendSideEffect(ProfileSideEffect.NavigateToSignOut)
            }
            is ProfileEvent.OnLogout -> {
                removeInfo()
            }
        }
    }
}