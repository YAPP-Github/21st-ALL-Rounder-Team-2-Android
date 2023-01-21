package com.yapp.gallery.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.gallery.common.theme.color_black
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.widget.model.CategoryUiState
import com.yapp.gallery.profile.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryDeleteDialog(
    onDismissRequest : () -> Unit,
    onDelete : () -> Unit
){
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
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                // 카테고리 타이틀
                Text(text = stringResource(id = R.string.category_delete_title), style = MaterialTheme.typography.h2
                    .copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = stringResource(id = R.string.category_delete_guide), style = MaterialTheme.typography.h4
                    .copy(color = color_gray600))
                Spacer(modifier = Modifier.height(30.dp))

                // 예, 아니오 버튼
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp)) {
                    Button(onClick = onDismissRequest,
                        shape = RoundedCornerShape(size = 50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = color_gray600),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "아니오", style = MaterialTheme.typography.h2.copy(
                            color = color_black, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = onDelete,
                        shape = RoundedCornerShape(size = 50.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "예", style = MaterialTheme.typography.h2.copy(
                            color = color_black, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}