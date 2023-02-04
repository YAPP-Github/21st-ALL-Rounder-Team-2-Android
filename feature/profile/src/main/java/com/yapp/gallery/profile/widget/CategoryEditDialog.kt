package com.yapp.gallery.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.profile.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryEditDialog(
    category: String,
    onEditCategory: (String) -> Unit,
    onDismissRequest: () -> Unit,
    checkEditable: (String, String) -> Unit,
    categoryState: BaseState<Boolean>
) {
    val categoryEdit = rememberSaveable{
        mutableStateOf(category)
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
                    modifier = Modifier.size(20.dp), tint = color_gray400
                )
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.category_edit_title), style = MaterialTheme.typography.h2
                    .copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column {
                    OutlinedTextField(
                        value = categoryEdit.value,
                        onValueChange = {
                            categoryEdit.value = it
                            checkEditable(category, categoryEdit.value)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.category_edit_hint), style =
                                MaterialTheme.typography.h3.copy(color = color_gray700)
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.primary,
                            textColor = color_white
                        ),
                        isError = categoryState is BaseState.Error,
                        trailingIcon = {
                            Row {
                                Text(
                                    text = "${categoryEdit.value.length}",
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
                    when(categoryState){
                        is BaseState.Error ->
                            Text(text = categoryState.message ?: "",
                                style = MaterialTheme.typography.h4.copy(color = Color.Red),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        else -> Spacer(modifier = Modifier.height(18.dp))
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
                Button(onClick = {
                    onEditCategory(categoryEdit.value)
                    onDismissRequest() },
                    shape = RoundedCornerShape(size = 50.dp),
                    enabled = (categoryState as? BaseState.Success<Boolean>)?.value ?: false
                ) {
                    Text(text = stringResource(id = R.string.category_edit_btn), style = MaterialTheme.typography.h2.copy(
                        color = color_black, fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

        }
    }
}