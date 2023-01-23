package com.yapp.gallery.profile.screen.profile

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.color_background
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.common.widget.ConfirmDialog
import com.yapp.gallery.domain.entity.profile.User
import com.yapp.gallery.profile.R

@Composable
fun ProfileScreen(
    popBackStack : () -> Unit,
    navigateToManage: () -> Unit,
    viewModel : ProfileViewModel
){
    val user : BaseState<User> by viewModel.userData.collectAsState()

    val context = LocalContext.current

    val logoutDialogShown = remember { mutableStateOf(false) }
    val withdrawalDialogShown = remember { mutableStateOf(false) }
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
        }
    ) { paddingValues ->  
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(38.dp))
            // 기본 닉네임 및 회원정보 row
            Row(modifier = Modifier
                .fillMaxWidth()) {
                Column {
                    Row {
                        Text(text = (user as? BaseState.Success<User>)?.value?.name ?: "",
                            style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.SemiBold))
                        Text(text = "이", style = MaterialTheme.typography.h1)
                    }
                    Text(text = "기록한 전시", style = MaterialTheme.typography.h1)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "00개의 전시 기록", style = MaterialTheme.typography.h3)
            }
            Spacer(modifier = Modifier.height(32.dp))

            // 카테고리 관리 버튼
            Button(
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = navigateToManage) {
                Text(text = stringResource(id = R.string.category_manage_btn), style = MaterialTheme.typography.h3.copy(
                    color = color_background, fontWeight = FontWeight.SemiBold), modifier = Modifier.padding(vertical = 10.dp))
            }

            // 각 기능 버튼
            Spacer(modifier = Modifier.height(32.dp))
            ProfileFeature(featureName = stringResource(id = R.string.feature_profile_edit), onFeatureClick = { /*TODO*/ }, isLast = false)
            ProfileFeature(featureName = stringResource(id = R.string.feature_announce), onFeatureClick = { /*TODO*/ }, isLast = false)
            ProfileFeature(featureName = stringResource(id = R.string.feature_service_legacy), onFeatureClick = { /*TODO*/ }, isLast = false)
            ProfileFeature(featureName = stringResource(id = R.string.feature_private_legacy), onFeatureClick = { /*TODO*/ }, isLast = false)
            ProfileFeature(featureName = stringResource(id = R.string.feature_logout), onFeatureClick = { logoutDialogShown.value = true }, isLast = false)
            ProfileFeature(featureName = stringResource(id = R.string.feature_withdraw), onFeatureClick = { /*TODO*/ }, isLast = true)


            // 로그아웃 다이얼로그
            if (logoutDialogShown.value){
                ConfirmDialog(
                    title = stringResource(id = R.string.logout_dialog_title),
                    subTitle = stringResource(id = R.string.logout_dialog_guide),
                    onDismissRequest = { logoutDialogShown.value = false },
                    onConfirm = { viewModel.logout() }
                )
            }
        }
    }
}

@Composable
fun ProfileFeature(
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

fun showToast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}