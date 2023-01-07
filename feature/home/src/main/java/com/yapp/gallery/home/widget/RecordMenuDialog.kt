package com.yapp.gallery.home.widget

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.home.R
import com.yapp.gallery.home.screen.CategoryUiState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecordMenuDialog(
    onCameraClick : () -> Unit,
    onGalleryClick : () -> Unit,
    onDismissRequest : () -> Unit,
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
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.record_menu_title), style = MaterialTheme.typography.h2
                    .copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCameraClick)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(painter = painterResource(id = R.drawable.ic_camera), contentDescription = "camera",
                            tint = color_mainGreen
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = stringResource(id = R.string.record_camera_label), style = MaterialTheme.typography
                            .h3.copy(color = color_gray400))
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }


                Divider(modifier = Modifier.fillMaxWidth(), color = color_gray900, thickness = 0.8.dp)

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable (onClick = onGalleryClick)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(painter = painterResource(id = R.drawable.ic_gallery), contentDescription = "gallery",
                            tint = color_mainGreen
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = stringResource(id = R.string.record_gallery_label), style = MaterialTheme.typography
                            .h3.copy(color = color_gray400))
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}