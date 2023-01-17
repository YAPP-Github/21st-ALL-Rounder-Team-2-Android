package com.yapp.gallery.profile.screen.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yapp.gallery.common.theme.color_gray300
import com.yapp.gallery.common.theme.color_gray500
import com.yapp.gallery.common.theme.color_gray700
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.profile.R

@Composable
fun CategoryManageScreen(
    popBackStack : () -> Unit
){
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = {
                    Text(
                        text = stringResource(id = R.string.category_manage_btn),
                        style = MaterialTheme.typography.h2.copy(
                            fontWeight = FontWeight.SemiBold)) },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {  }) {
                        Text(
                            text = stringResource(id = R.string.category_add),
                            style = MaterialTheme.typography.h3.copy(
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            CategoryListTile(categoryName = "카테고리1", categoryCnt = 5, false)
            CategoryListTile(categoryName = "카테고리2", categoryCnt = 5, false)
            CategoryListTile(categoryName = "카테고리3", categoryCnt = 5, false)
            CategoryListTile(categoryName = "카테고리4", categoryCnt = 5, false)
            CategoryListTile(categoryName = "카테고리5", categoryCnt = 5, true)
        }
    }
}

@Composable
fun CategoryListTile(
    categoryName: String,
    categoryCnt: Int,
    isLast: Boolean
){
    val tempList = listOf("전시01", "전시02", "전시03", "전시04", "전시05")

    Column (modifier = Modifier.fillMaxWidth()){
        // 카테고리 브리프 정보 및 첫 행
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Menu, contentDescription = null,
                    tint = color_gray500, modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = categoryName,
                style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "$categoryCnt${stringResource(id = R.string.category_exhibit_cnt)}",
                style = MaterialTheme.typography.h4.copy(color = color_gray500)
            )
            Spacer(modifier = Modifier.weight(1f))

            // 편집 및 삭제
            Row(modifier = Modifier.padding(end = 8.dp)) {
                Text(text = stringResource(id = R.string.category_edit),
                    style = MaterialTheme.typography.h4.copy(color = color_gray500),
                    modifier = Modifier
                        .clickable { }
                        .padding(8.dp)
                )

                Text(text = stringResource(id = R.string.category_remove),
                    style = MaterialTheme.typography.h4.copy(color = color_gray500),
                    modifier = Modifier
                        .clickable { }
                        .padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        // 카테고리에 담긴 전시 정보
        LazyRow(modifier = Modifier.padding(start = 48.dp, end = 16.dp)) {
            items(tempList){item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Image(painter = painterResource(id = R.drawable.exhibit_test),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = item, style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Medium,
                        color = color_gray300)
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (!isLast){
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                color = color_gray700,
                thickness = 0.4.dp
            )
        }
    }

}