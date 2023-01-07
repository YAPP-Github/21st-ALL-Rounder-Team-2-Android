package com.yapp.gallery.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.gallery.common.theme.color_gray400
import com.yapp.gallery.common.theme.color_gray700
import com.yapp.gallery.common.theme.color_gray900
import com.yapp.gallery.common.theme.color_mainBlue
import com.yapp.gallery.domain.entity.home.ExhibitInfo
import com.yapp.gallery.home.R
import com.yapp.gallery.home.screen.ExhibitInfoViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TempStorageDialog(
    onDismissRequest : () -> Unit,
    viewModel : ExhibitInfoViewModel
){
    val editMode = remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = {editMode.value = !editMode.value},
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 2.dp, end = 2.dp)
            ) {
                Text(text = if (!editMode.value) stringResource(id = R.string.temp_storage_edit)
                        else stringResource(id = R.string.temp_storage_edit_finish),
                    style = MaterialTheme.typography.h3.copy(color = color_gray400))
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.Center){
                    Text(text = stringResource(id = R.string.temp_storage_title),
                        style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold))
                    viewModel.tempStorageList.apply {
                        if (this.isNotEmpty()){
                            Text(
                                text = " (${this.size}) ",
                                style = MaterialTheme.typography.h2.copy(
                                    fontWeight = FontWeight.SemiBold, color = color_mainBlue)
                            )
                        }
                    }
                }
                viewModel.tempStorageList.apply {
                    if (this.isNotEmpty()){
                        Spacer(modifier = Modifier.height(18.dp))
                        LazyColumn{
                            itemsIndexed(this@apply){ index, item ->
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.exhibitName,
                                        style = MaterialTheme.typography.h3.copy(
                                            fontWeight = FontWeight.Medium, color = color_gray400),
                                        modifier = Modifier.padding(vertical = 18.dp)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    if (editMode.value){
                                        IconButton(onClick = { viewModel.deleteTempStorageItem(index) }) {
                                            Icon(painter = painterResource(id = R.drawable.ic_delete),
                                                contentDescription = "delete", tint = color_gray700)
                                        }
                                    } else {
                                        Text(
                                            text = item.exhibitDate,
                                            style = MaterialTheme.typography.h5.copy(color = color_gray700),
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                    }

                                }
                                if (index != this@apply.size - 1){
                                    Divider(color = color_gray900, modifier = Modifier.fillMaxWidth(),
                                        thickness = 0.8.dp)
                                } else{
                                    Spacer(modifier = Modifier.height(18.dp))
                                }
                            }
                        }
                    } else {
                        // Todo : 임시 보관함이 비어있을 때 레이아웃
                    }

                }
            }
        }
    }
}