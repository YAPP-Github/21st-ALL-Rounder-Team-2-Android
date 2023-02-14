package com.yapp.gallery.profile.screen.notice

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.gallery.common.theme.color_gray700
import com.yapp.gallery.common.theme.color_gray900
import com.yapp.gallery.common.theme.grey_bdbdbd
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.profile.R

@Composable
fun NoticeDetailScreen(
    noticeTitle : String,
    noticeContent : String,
    noticeDate : String,
    popBackStack : () -> Unit
){
    Scaffold(
        topBar = {
            CenterTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = {
                    Text(
                        text = stringResource(id = R.string.notice_appbar_title),
                        style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = noticeTitle,
                style = MaterialTheme.typography.h1.copy(fontSize = 22.sp, lineHeight = 28.6.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = noticeDate,
                style = MaterialTheme.typography.h4.copy(color = color_gray700)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Divider(modifier = Modifier.fillMaxWidth(), color = color_gray900, thickness = 0.4.dp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = noticeContent, style = MaterialTheme.typography.h3.copy(
                color = grey_bdbdbd,
                lineHeight = 22.4.sp)
            )
        }
    }
}
