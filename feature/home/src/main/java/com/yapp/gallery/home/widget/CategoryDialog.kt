package com.yapp.gallery.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.home.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryDialog(
    onCreateCategory : (String) -> Unit,
    onDismissRequest : () -> Unit
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
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.category_title), fontFamily = pretendard, fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(value = categoryName.value,
                    onValueChange = {categoryName.value = it},
                    placeholder = { Text(text = stringResource(id = R.string.category_hint), color = color_gray700, fontFamily = pretendard,
                        fontSize = 16.sp
                    )},
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        textColor = color_white
                    ),
                    isError = categoryName.value.length > 20,
                    trailingIcon = {
                        Row {
                            Text(text = "${categoryName.value.length}", color = MaterialTheme.colors.primary,
                                fontSize = 14.sp
                            )
                            Text(text = "/20",
                                modifier = Modifier.padding(end = 10.dp), color = color_gray600,
                                fontSize = 14.sp
                            )
                        }

                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(49.dp))
                Button(onClick = {onCreateCategory(categoryName.value)},
                    shape = RoundedCornerShape(size = 50.dp),
                    enabled = categoryName.value.isNotEmpty() && categoryName.value.length <= 20
                ) {
                    Text(text = stringResource(id = R.string.category_create_btn), fontFamily = pretendard, fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp, color = color_black,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
            }

        }
    }
}