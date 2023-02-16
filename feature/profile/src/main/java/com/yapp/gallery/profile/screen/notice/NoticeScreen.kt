package com.yapp.gallery.profile.screen.notice

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.domain.entity.notice.NoticeItem
import com.yapp.gallery.profile.R

@Composable
fun NoticeScreen(
    navigateToDetail : (NoticeItem) -> Unit,
    popBackStack : () -> Unit,
    viewModel: NoticeViewModel = hiltViewModel(),
    context : Context = LocalContext.current
){
    val noticeListState : BaseState<List<NoticeItem>> by viewModel.noticeListState.collectAsState()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(viewModel.errors){
        viewModel.errors.collect{
            scaffoldState.snackbarHostState.showSnackbar(it.asString(context))
        }
    }

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
        },
        scaffoldState = scaffoldState
    ) {paddingValues ->
        when (noticeListState) {
            is BaseState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(20.dp)
                ) {
                    LazyColumn {
                        items((noticeListState as BaseState.Success<List<NoticeItem>>).value) { notice ->
                            NoticeTile(notice = notice, onClick = { navigateToDetail(notice) })
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                color = color_gray900,
                                thickness = 0.4.dp
                            )
                        }
                    }
                }
            }
            else -> {
                Column(modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (noticeListState is BaseState.Loading){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .align(CenterHorizontally),
                            color = color_mainBlue
                        )
                    }
                    else {
                        Text(
                            text = stringResource(id = R.string.notice_empty_guide),
                            style = MaterialTheme.typography.h3.copy(color = color_gray600),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }


    }
}

@Composable
private fun NoticeTile(
    notice : NoticeItem,
    onClick : (NoticeItem) -> Unit
){
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(notice) })
            .padding(vertical = 16.dp)
    ) {
        val (text1, text2, text3) = createRefs()
        Text(
            text = notice.title,
            style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.constrainAs(text1){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )

        Text(
            text = notice.contents,
            style = MaterialTheme.typography.h4.copy(
                color = color_gray500, lineHeight = 19.sp
            ),
            modifier = Modifier.constrainAs(text2){
                start.linkTo(text1.start)
                top.linkTo(text1.bottom, margin = 8.dp)
                end.linkTo(text3.start, margin = 40.dp)
                width = Dimension.fillToConstraints
            },
            maxLines = 2
        )

        Text(text = notice.date,
            style = MaterialTheme.typography.h5.copy(
                color = color_gray700
            ),
            modifier = Modifier.constrainAs(text3) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 1)
@Composable
private fun NoticeTilePreview(){
    GalleryTheme {
        NoticeTile(notice = NoticeItem(date = "2023-02-22", id = 1, title = "공지사항", contents = "내용"),
            onClick = {}
        )
    }
}