package com.yapp.gallery.saver

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.yapp.gallery.common.theme.*

@Composable
fun SaverView(
    onRetryListener: () -> Unit,
    saveToFile: (Uri) -> Unit,
    uri: Uri = Uri.EMPTY,
    uris: List<Uri> = emptyList()
) {

    Box(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()) {

        Image(
            painter = if(uris.isNotEmpty()) {
                rememberImagePainter(uris.first())
            } else {
                rememberImagePainter(uri)
            },
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        
        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 35.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = color_gray600,
                    disabledContentColor = color_gray900,
                    backgroundColor = color_popUpBottom,
                    contentColor = color_white
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                onClick = { onRetryListener.invoke() }
            ) {
                Text(text = stringResource(id = R.string.saver_continuation))
            }
            Button(
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = color_gray600,
                    disabledContentColor = color_gray900,
                    backgroundColor = color_mainGreen,
                    contentColor = color_black
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                onClick = { saveToFile(uri) }
            ) {
                Text(
                    text = stringResource(id = R.string.saver_save_to_file),
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard
                )
            }
        }
    }
}
