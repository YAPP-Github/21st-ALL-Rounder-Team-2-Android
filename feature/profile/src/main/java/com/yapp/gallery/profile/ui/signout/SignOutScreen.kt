package com.yapp.gallery.profile.ui.signout

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.theme.color_black
import com.yapp.gallery.common.theme.color_mainGreen
import com.yapp.gallery.common.theme.grey_bdbdbd
import com.yapp.gallery.common.theme.pretendard
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.profile.R

@Composable
fun SignOutScreen(
    popBackStack: () -> Unit,
    signOut: () -> Unit,
    loginType: String = "google",
    viewModel: SignOutViewModel = hiltViewModel()
){
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
                    viewModel.removeInfo(loginType)
                    signOut()
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
            .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = stringResource(id = R.string.sign_out_title),
                style = MaterialTheme.typography.h1.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.sign_out_guide),
                style = MaterialTheme.typography.h3.copy(
                    color = grey_bdbdbd,
                    lineHeight = 25.6.sp
                ),
            )
        }
    }  
}