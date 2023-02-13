package com.yapp.gallery.profile.screen.signout

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.common.widget.ConfirmDialog
import com.yapp.gallery.profile.R

@Composable
fun SignOutScreen(
    popBackStack: () -> Unit,
    signOut: () -> Unit,
    viewModel: SignOutViewModel = hiltViewModel()
){
    val signOutDialogShown = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterTopAppBar(modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                title = {
                    Text(
                        text = stringResource(id = R.string.sign_out_appbar_title),
                        style = MaterialTheme.typography.h2.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = color_mainGreen,
                    contentColor = color_black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 53.dp),
                onClick = {
                    signOutDialogShown.value = true
                },
            ) {
                Text(
                    text = stringResource(id = R.string.sign_out_btn),
                    modifier = Modifier.padding(vertical = 12.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    fontFamily = pretendard
                )
            }
        }
    ) {paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = stringResource(id = R.string.sign_out_guide),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            // 회원탈퇴 다이얼로그
            if (signOutDialogShown.value){
                // Todo : 서버에서 탈퇴까지 구현해야함
                ConfirmDialog(
                    title = stringResource(id = R.string.sign_out_dialog_title),
                    subTitle = stringResource(id = R.string.sign_out_dialog_guide),
                    onDismissRequest = { signOutDialogShown.value = false },
                    onConfirm = {
                        viewModel.removeInfo()
                        signOut()
                    }
                )
            }
        }
    }  
}