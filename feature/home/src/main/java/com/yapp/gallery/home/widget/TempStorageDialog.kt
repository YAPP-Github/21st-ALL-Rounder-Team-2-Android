package com.yapp.gallery.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.gallery.common.theme.color_gray400
import com.yapp.gallery.domain.entity.home.ExhibitInfo
import com.yapp.gallery.home.R
import com.yapp.gallery.home.screen.ExhibitInfoViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TempStorageDialog(
    onDismissRequest : () -> Unit,
    viewModel : ExhibitInfoViewModel
){
    val tempList : List<ExhibitInfo>? by viewModel.tempStorageList.collectAsState()

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
            TextButton(onClick = onDismissRequest,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 2.dp, horizontal = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.temp_storage_edit),
                    style = MaterialTheme.typography.h3.copy(color = color_gray400))
            }
            Column(modifier = Modifier.fillMaxWidth()) {

            }
        }
    }
}