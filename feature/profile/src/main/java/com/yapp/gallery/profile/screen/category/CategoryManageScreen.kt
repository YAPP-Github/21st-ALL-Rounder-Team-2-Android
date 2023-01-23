package com.yapp.gallery.profile.screen.category

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.widget.CategoryCreateDialog
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.common.widget.ConfirmDialog
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.profile.R
import com.yapp.gallery.profile.utils.DraggableItem
import com.yapp.gallery.profile.utils.rememberDragDropState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryManageScreen(
    popBackStack: () -> Unit,
    viewModel: CategoryManageViewModel = hiltViewModel(),
) {
    val categoryScreenState: BaseState<Boolean> by viewModel.categoryManageState.collectAsState()

    val categoryCreateDialogShown = remember { mutableStateOf(false) }

    var overscrollJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val dragDropState = rememberDragDropState(lazyListState = listState){ from, to ->
        viewModel.reorderItem(from, to)
    }

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
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { categoryCreateDialogShown.value = true },
                        enabled = categoryScreenState != BaseState.Loading
                    ) {
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
        if ((categoryScreenState as? BaseState.Success<Boolean>)?.value == true){
            Column(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(36.dp))
                // Todo : 임시 코드
                LazyColumn(
                    state = listState,
                    modifier = Modifier.pointerInput(dragDropState){
                        detectDragGesturesAfterLongPress(
                            onDrag = { change, offset ->
                                change.consume()
                                dragDropState.onDrag(offset = offset)

                                if (overscrollJob?.isActive == true)
                                    return@detectDragGesturesAfterLongPress

                                dragDropState
                                    .checkForOverScroll()
                                    .takeIf { it != 0f }
                                    ?.let {
                                        overscrollJob =
                                            scope.launch {
                                                dragDropState.state.animateScrollBy(
                                                    it*2f, tween(easing = FastOutLinearInEasing)
                                                )
                                            }
                                    }
                                    ?: run { overscrollJob?.cancel() }
                            },
                            onDragStart = { offset -> dragDropState.onDragStart(offset) },
                            onDragEnd = {
                                dragDropState.onDragInterrupted()
                                overscrollJob?.cancel()
                            },
                            onDragCancel = {
                                dragDropState.onDragInterrupted()
                                overscrollJob?.cancel()
                            }
                        )
                    }
                ) {
                    itemsIndexed(viewModel.categoryList) { index, item ->
                        DraggableItem(dragDropState = dragDropState, index = index) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                            CategoryListTile(
                                category = item,
                                isLast = index == viewModel.categoryList.size - 1,
                                elevation = elevation
                            ) { viewModel.deleteCategory(item) }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (categoryScreenState is BaseState.Loading) {
                    // 로딩 중
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = color_mainBlue
                    )
                } else {
                    // 카테고리 리스트가 빈 리스트인 경우
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.category_manage_empty_guide),
                            style = MaterialTheme.typography.h3.copy(color = color_gray600),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // 카테고리 만들기 버튼
                        Surface(
                            shape = RoundedCornerShape(71.dp),
                            color = MaterialTheme.colors.background,
                            border = BorderStroke(1.dp, color = Color(0xFFA7C5F9)),
                            onClick = { categoryCreateDialogShown.value = true }
                        ) {
                            Text(
                                text = stringResource(id = R.string.category_manage_create),
                                style = MaterialTheme.typography.h3.copy(
                                    color = Color(0xFFA7C5F9),
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(
                                    horizontal = 24.dp,
                                    vertical = 12.dp
                                )
                            )
                        }
                    }
                }
            }
        }

        if (categoryCreateDialogShown.value) {
            CategoryCreateDialog(
                onCreateCategory = { viewModel.createCategory(it) },
                onDismissRequest = { categoryCreateDialogShown.value = false },
                checkCategory = { viewModel.checkCategory(it) },
                categoryState = viewModel.categoryState.collectAsState()
            )
        }
    }
}

@Composable
fun CategoryListTile(
    category: CategoryItem,
    isLast: Boolean,
    elevation: Dp,
    onDelete: () -> Unit,
) {
    val tempList = listOf("전시01", "전시02", "전시03", "전시04", "전시05")
    val categoryDeleteDialogShown = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .shadow(elevation)
        .background(color = color_background)) {
        // 카테고리 브리프 정보 및 첫 행
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Menu, contentDescription = null,
                    tint = color_gray500, modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = category.name,
                style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${category.id}${stringResource(id = R.string.category_exhibit_cnt)}",
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
                        .clickable { categoryDeleteDialogShown.value = true }
                        .padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        // 카테고리에 담긴 전시 정보
        LazyRow(modifier = Modifier.padding(start = 48.dp, end = 16.dp)) {
            items(tempList) { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.exhibit_test),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = item, style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Medium,
                            color = color_gray300
                        )
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (!isLast) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = color_gray700,
                thickness = 0.4.dp
            )
        }

        // 카테고리 삭제 다이얼로그
        if (categoryDeleteDialogShown.value) {
            ConfirmDialog(
                title = stringResource(id = R.string.category_delete_title),
                subTitle = stringResource(id = R.string.category_delete_guide),
                onDismissRequest = { categoryDeleteDialogShown.value = false },
                onConfirm = {
                    categoryDeleteDialogShown.value = false
                    onDelete()
                }
            )
        }
    }

}