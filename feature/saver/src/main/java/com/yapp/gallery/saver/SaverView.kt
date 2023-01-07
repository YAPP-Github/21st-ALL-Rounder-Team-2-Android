package com.yapp.gallery.saver

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun SaverView(
    saveToFile: (Uri) -> Unit,
    uri: Uri
) {

    Box(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()) {

        Image(
            painter = rememberImagePainter(uri),
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
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                ,
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(id = R.string.saver_continuation))
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                onClick = { saveToFile(uri) }
            ) {
                Text(text = stringResource(id = R.string.saver_save_to_file))
            }
        }
    }
}
