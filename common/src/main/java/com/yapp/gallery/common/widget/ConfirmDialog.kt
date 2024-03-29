package com.yapp.gallery.common.widget

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.gallery.common.theme.color_black
import com.yapp.gallery.common.theme.color_gray400
import com.yapp.gallery.common.theme.color_gray600

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConfirmDialog(
    title: String,
    subTitle: String?,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    important: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = if(important) DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = false, dismissOnClickOutside = false)
            else DialogProperties(usePlatformDefaultWidth = false)
    ) {
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
            IconButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = null,
                    modifier = Modifier.size(20.dp), tint = color_gray400
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.h2.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 25.2.sp
                        ),
                    textAlign = TextAlign.Center
                )
                subTitle?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subTitle, style = MaterialTheme.typography.h4
                            .copy(color = color_gray600), textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                // 예, 아니오 버튼
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 29.dp)
                ) {
                    Button(
                        onClick = onDismissRequest,
                        shape = RoundedCornerShape(size = 50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = color_gray600),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "아니오", style = MaterialTheme.typography.h2.copy(
                                color = color_black, fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(size = 50.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "예", style = MaterialTheme.typography.h2.copy(
                                color = color_black, fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}