package com.yapp.gallery.profile.ui.nickname

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.profile.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NicknameScreen(
    popBackStack: () -> Unit,
    nicknameUpdate: (String) -> Unit,
    viewModel: NicknameViewModel = hiltViewModel(),
){
    val scaffoldState = rememberScaffoldState()

    val nicknameState : NicknameState by viewModel.nicknameState.collectAsState()

    Scaffold(
        topBar = {
            CenterTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = {
                    Text(
                        text = stringResource(id = R.string.nickname_appbar_title),
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
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = color_gray600,
                    disabledContentColor = color_gray900,
                    backgroundColor = color_mainGreen,
                    contentColor = color_black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 53.dp),
                onClick = {
                    viewModel.updateNickname()
                    nicknameUpdate(viewModel.nickname.value)
                },
                enabled = nicknameState is NicknameState.Normal
            ) {
                Text(
                    text = stringResource(id = R.string.nickname_appbar_title),
                    modifier = Modifier.padding(vertical = 12.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    fontFamily = pretendard
                )
            }
        }
    ){paddingValues ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
        )
        {
            Spacer(modifier = Modifier.height(48.dp))
            BasicTextField(
                value = viewModel.nickname.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    viewModel.changeNickname(it)
                },
                cursorBrush = SolidColor(color_white),
                textStyle = MaterialTheme.typography.h3
            ){
                TextFieldDefaults.TextFieldDecorationBox(
                    value = viewModel.nickname.value,
                    innerTextField = it,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = MutableInteractionSource(),
                    enabled = true,
                    singleLine = true,
                    contentPadding = PaddingValues(0.dp),
                    isError = nicknameState is NicknameState.Error,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.nickname_guide), style =
                            MaterialTheme.typography.h3.copy(color = color_gray700)
                        )
                    },
                    trailingIcon = {
                        Row {
                            Text(
                                text = "${viewModel.nickname.value.length}",
                                style = MaterialTheme.typography
                                    .h4.copy(color = color_mainGreen)
                            )
                            Text(
                                text = "/10",
                                modifier = Modifier.padding(end = 10.dp),
                                style = MaterialTheme.typography
                                    .h4.copy(color = color_gray600)
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                color = if (nicknameState is NicknameState.Error) MaterialTheme.colors.error else color_gray600,
                modifier = Modifier.fillMaxWidth(),
                thickness = 0.8.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (nicknameState is NicknameState.Error){
                Text(text = (nicknameState as NicknameState.Error).message,
                    style = MaterialTheme.typography.h4.copy(color = Color.Red),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}