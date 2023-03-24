package com.yapp.gallery.profile.ui.profile

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.profile.ui.profile.ProfileContract.*
import com.yapp.gallery.common.theme.color_background
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.common.widget.ConfirmDialog
import com.yapp.gallery.profile.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    navigateToManage: () -> Unit,
    navigateToNickname: (String) -> Unit,
    navigateToNotice: () -> Unit,
    navigateToLegacy: () -> Unit,
    navigateToSignOut: () -> Unit,
    navigateToLogin: () -> Unit,
    popBackStack: () -> Unit,
    editedNicknameData: LiveData<String>,
    viewModel: ProfileViewModel = hiltViewModel(),
    context : Context = LocalContext.current
) {
    val profileState: ProfileState by viewModel.viewState.collectAsStateWithLifecycle()

    val logoutDialogShown = remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(viewModel.errors) {
        viewModel.errors.collectLatest {
            scaffoldState.snackbarHostState.showSnackbar(it.asString(context))
        }
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest {
            when (it) {
                is ProfileSideEffect.NavigateToLogin -> {
                    navigateToLogin()
                }
                is ProfileSideEffect.NavigateToNickname -> {
                    editedNicknameData.value?.let { name ->
                        navigateToNickname(name)
                    } ?: run {
                        if (profileState is ProfileState.Success) {
                            navigateToNickname((profileState as ProfileState.Success).user.name)
                        }
                    }
                }
                is ProfileSideEffect.NavigateToNotice -> {
                    navigateToNotice()
                }
                is ProfileSideEffect.NavigateToLegacy -> {
                    navigateToLegacy()
                }
                is ProfileSideEffect.NavigateToManage -> {
                    navigateToManage()
                }

                is ProfileSideEffect.NavigateToSignOut -> {
                    navigateToSignOut()
                }
            }
        }
    }


    ProfileScaffold(
        profileState = profileState,
        editedNickname = editedNicknameData.value,
        onManageClick = { viewModel.setEvent(ProfileEvent.OnManageClick) },
        onNicknameClick = { viewModel.setEvent(ProfileEvent.OnNicknameClick) },
        onNoticeClick = { viewModel.setEvent(ProfileEvent.OnNoticeClick) },
        onLegacyClick = { viewModel.setEvent(ProfileEvent.OnLegacyClick) },
        onSignOutClick = { viewModel.setEvent(ProfileEvent.OnSignOutClick) },
        onLogoutClick = { logoutDialogShown.value = true },
        popBackStack = popBackStack,
        scaffoldState = scaffoldState
    )

    // 로그아웃 다이얼로그
    if (logoutDialogShown.value){
        ConfirmDialog(
            title = stringResource(id = R.string.logout_dialog_title),
            onDismissRequest = { logoutDialogShown.value = false },
            onConfirm = { viewModel.setEvent(ProfileEvent.OnLogout) }
        )
    }
}

@Composable
private fun ProfileScaffold(
    profileState: ProfileState,
    editedNickname: String?,
    onManageClick: () -> Unit,
    onNicknameClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onLegacyClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onLogoutClick: () -> Unit,
    scaffoldState: ScaffoldState,
    popBackStack: () -> Unit
){
    val user = (profileState as? ProfileState.Success)?.user

    Scaffold(
        topBar = {
            CenterTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = {
                    Text(
                        text = stringResource(id = R.string.profile_appbar_title),
                        style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(38.dp))
            // 기본 닉네임 및 회원정보 row
            Row(modifier = Modifier
                .fillMaxWidth()) {
                Column {
                    val nicknameString = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append(editedNickname?: user?.name ?: "")
                        }
                        append("이")
                    }
                    Text(text = nicknameString, style = MaterialTheme.typography.h1)

                    Text(text = "기록한 전시", style = MaterialTheme.typography.h1)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${user?.exhibitCount ?: ""}개의 전시 기록",
                    style = MaterialTheme.typography.h3)
            }
            Spacer(modifier = Modifier.height(32.dp))

            // 카테고리 관리 버튼
            Button(
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = onManageClick
            ) {
                Text(text = stringResource(id = R.string.category_manage_btn), style = MaterialTheme.typography.h3.copy(
                    color = color_background, fontWeight = FontWeight.SemiBold), modifier = Modifier.padding(vertical = 10.dp))
            }

            // 각 기능 버튼
            Spacer(modifier = Modifier.height(32.dp))
            ProfileFeature(
                featureName = stringResource(id = R.string.feature_profile_edit),
                onFeatureClick = onNicknameClick,
                isLast = false
            )
            ProfileFeature(
                featureName = stringResource(id = R.string.feature_announce),
                onFeatureClick = onNoticeClick,
                isLast = false
            )
            ProfileFeature(
                featureName = stringResource(id = R.string.feature_legacy),
                onFeatureClick = onLegacyClick,
                isLast = false
            )
            ProfileFeature(
                featureName = stringResource(id = R.string.feature_logout),
                onFeatureClick = onLogoutClick,
                isLast = false
            )
            ProfileFeature(
                featureName = stringResource(id = R.string.feature_sign_out),
                onFeatureClick = onSignOutClick,
                isLast = true
            )

        }
    }
}

@Composable
private fun ProfileFeature(
    featureName: String,
    onFeatureClick : () -> Unit,
    isLast: Boolean
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onFeatureClick)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = featureName, style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium))
        Spacer(modifier = Modifier.height(16.dp))
        if (!isLast)
            Divider(color = color_gray600, thickness = 0.4.dp, modifier = Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    GalleryTheme {
        ProfileScaffold(
            profileState = ProfileState.Initial,
            editedNickname = null,
            onManageClick = { },
            onNicknameClick = { },
            onNoticeClick = { },
            onLegacyClick = { },
            onSignOutClick = { },
            onLogoutClick = { },
            scaffoldState = rememberScaffoldState(),
            popBackStack = {}
        )
    }
}