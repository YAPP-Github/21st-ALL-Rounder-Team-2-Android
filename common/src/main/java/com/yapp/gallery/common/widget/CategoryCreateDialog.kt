package com.yapp.gallery.common.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.gallery.common.R
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.model.BaseState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryCreateDialog(
    onCreateCategory: (String) -> Unit,
    onDismissRequest: () -> Unit,
    checkCategory: (String) -> Unit,
    categoryState: State<BaseState<Boolean>>
){
    val categoryName = rememberSaveable {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            IconButton(onClick = onDismissRequest,
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null,
                    modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.category_title), style = MaterialTheme.typography.h2
                    .copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column {
                    OutlinedTextField(
                        value = categoryName.value,
                        onValueChange = {
                            categoryName.value = it
                            checkCategory(it)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.category_hint), style =
                                MaterialTheme.typography.h3.copy(color = color_gray700)
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.primary,
                            textColor = color_white
                        ),
                        isError = categoryState.value is BaseState.Error,
                        trailingIcon = {
                            Row {
                                Text(
                                    text = "${categoryName.value.length}",
                                    style = MaterialTheme.typography
                                        .h4.copy(color = color_mainGreen)
                                )
                                Text(
                                    text = "/20",
                                    modifier = Modifier.padding(end = 10.dp),
                                    style = MaterialTheme.typography
                                        .h4.copy(color = color_gray600)
                                )
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    when(categoryState.value){
                        is BaseState.Error ->
                            Text(text = (categoryState.value as BaseState.Error).message ?: "",
                                style = MaterialTheme.typography.h4.copy(color = Color.Red),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        else -> Text(text = stringResource(id = R.string.category_create_guide),
                                style = MaterialTheme.typography.h4.copy(color = color_mainGreen),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
                Button(onClick = {
                        onCreateCategory(categoryName.value)
                        onDismissRequest() },
                    shape = RoundedCornerShape(size = 50.dp),
                    enabled = (categoryState.value as? BaseState.Success<Boolean>)?.value ?: false
                ) {
                    Text(text = stringResource(id = R.string.category_create_btn), style = MaterialTheme.typography.h2.copy(
                        color = color_black, fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

        }
    }
}