package com.yapp.gallery.profile.screen.signout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.gallery.common.theme.color_black
import com.yapp.gallery.common.theme.color_mainGreen
import com.yapp.gallery.common.theme.pretendard
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.profile.R

@Composable
fun SignOutCompleteScreen(
    navigateToLogin : () -> Unit
){
    BackHandler(enabled = true, onBack = navigateToLogin)

    Scaffold(
        topBar = {
            CenterTopAppBar(modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                title = {
                    Text(
                        text = stringResource(id = R.string.sign_out_complete_appbar_title),
                        style = MaterialTheme.typography.h2.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
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
                    navigateToLogin()
                },
            ) {
                Text(
                    text = "완료",
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
            .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = stringResource(id = R.string.sign_out_complete),
                style = MaterialTheme.typography.h1.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                ),
            )
        }
    }
}