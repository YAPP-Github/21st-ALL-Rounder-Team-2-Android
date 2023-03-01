package com.yapp.gallery.profile.screen.category

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.widget.CategoryCreateDialog
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.common.widget.ConfirmDialog
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.profile.R
import com.yapp.gallery.profile.utils.DraggableItem
import com.yapp.gallery.profile.utils.rememberDragDropState
import com.yapp.gallery.profile.widget.CategoryEditDialog
import com.yapp.gallery.profile.widget.CustomSnackbarHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun CategoryManageScreen(
    popBackStack: () -> Unit,
    viewModel: CategoryManageViewModel = hiltViewModel(),
) {
    val categoryScreenState: BaseState<Boolean> by viewModel.categoryManageState.collectAsState()

    val categoryCreateDialogShown = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val snackState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.errors.collect { error ->
            snackState.showSnackbar(
                message = error.asString(context), duration = SnackbarDuration.Short
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        CenterTopAppBar(modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background,
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
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                    )
                }
            },
            actions = {
                TextButton(
                    onClick = {
                        if (viewModel.categoryList.size < 5) categoryCreateDialogShown.value = true
                        else scope.launch {
                            snackState.showSnackbar(
                                message = "최대 5개까지 생성 가능해요!", duration = SnackbarDuration.Short
                            )
                        }
                    }, enabled = categoryScreenState != BaseState.Loading
                ) {
                    Text(
                        text = stringResource(id = R.string.category_add),
                        style = MaterialTheme.typography.h3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                    )
                }
            })

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 카테고리 정보 뷰
            CategoryListView(
                categoryScreenState = categoryScreenState,
                viewModel = viewModel,
                categoryCreateDialogShown = categoryCreateDialogShown,
                scope = scope
            )

            // 커스텀 Snackbar
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                CustomSnackbarHost(snackbarHostState = snackState)
            }

            // 카테고리 생성 다이얼로그

            if (categoryCreateDialogShown.value) {
                CategoryCreateDialog(onCreateCategory = { viewModel.createCategory(it) },
                    onDismissRequest = { categoryCreateDialogShown.value = false },
                    checkCategory = { viewModel.checkCategory(it) },
                    categoryState = viewModel.categoryState.collectAsState().value
                )
            }
        }

    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun CategoryListView(
    categoryScreenState: BaseState<Boolean>,
    viewModel : CategoryManageViewModel,
    categoryCreateDialogShown: MutableState<Boolean>,
    scope: CoroutineScope,
    listState: LazyListState = rememberLazyListState(),
) {
    val dragDropState = rememberDragDropState(lazyListState = listState) { from, to ->
        viewModel.reorderItem(from, to)
    }
    var overscrollJob by remember { mutableStateOf<Job?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        if ((categoryScreenState as? BaseState.Success<Boolean>)?.value == true) {
            Spacer(modifier = Modifier.height(36.dp))

            LazyColumn(state = listState, modifier = Modifier.pointerInput(dragDropState) {
                detectDragGesturesAfterLongPress(onDrag = { change, offset ->
                    change.consume()
                    dragDropState.onDrag(offset = offset)

                    if (overscrollJob?.isActive == true) return@detectDragGesturesAfterLongPress

                    dragDropState.checkForOverScroll().takeIf { it != 0f }?.let {
                        overscrollJob = scope.launch {
                            dragDropState.state.animateScrollBy(
                                it * 2f, tween(easing = FastOutLinearInEasing)
                            )
                        }
                    } ?: run { overscrollJob?.cancel() }
                },
                    onDragStart = { offset -> dragDropState.onDragStart(offset) },
                    onDragEnd = {
                        dragDropState.onDragInterrupted()
                        overscrollJob?.cancel()
                    },
                    onDragCancel = {
                        dragDropState.onDragInterrupted()
                        overscrollJob?.cancel()
                    })
            }) {
                itemsIndexed(viewModel.categoryList) { index, item ->
                    // Draggable Item
                    DraggableItem(dragDropState = dragDropState, index = index) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 10.dp else 0.dp)
                        CategoryListTile(
                            category = item,
                            categoryPostState = viewModel.categoryPostState[index],
                            elevation = elevation,
                            viewModel = viewModel,
                            index = index
                        )
                    }
                    // Divider
                    if (index != viewModel.categoryList.size - 1) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            color = color_gray700,
                            thickness = 0.4.dp
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (categoryScreenState is BaseState.Loading) {
                    // 로딩 중
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp), color = color_mainBlue
                    )
                } else {
                    // 카테고리 리스트가 빈 리스트인 경우
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.category_manage_empty_guide),
                            style = MaterialTheme.typography.h3.copy(
                                color = color_gray600,
                                lineHeight = 24.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // 카테고리 만들기 버튼
                        Surface(shape = RoundedCornerShape(71.dp),
                            color = MaterialTheme.colors.background,
                            border = BorderStroke(1.dp, color = Color(0xFFA7C5F9)),
                            onClick = { categoryCreateDialogShown.value = true }) {
                            Text(
                                text = stringResource(id = R.string.category_manage_create),
                                style = MaterialTheme.typography.h3.copy(
                                    color = Color(0xFFA7C5F9), fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(
                                    horizontal = 24.dp, vertical = 12.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryListTile(
    category: CategoryItem,
    categoryPostState : CategoryPostState,
    elevation: Dp,
    viewModel: CategoryManageViewModel,
    index: Int
) {
    val categoryEditDialogShown = remember { mutableStateOf(false) }
    val categoryDeleteDialogShown = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(ambientColor = color_popUpBottom, elevation = elevation)
            .animateContentSize(animationSpec = tween())
            .background(color = color_background)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        // 카테고리 브리프 정보 및 첫 행
        ConstraintLayout(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()
        ) {
            val (button, row, text1, text2) = createRefs()
            IconButton(onClick = { viewModel.expandCategory(index) },
                modifier = Modifier
                    .size(18.dp)
                    .constrainAs(button) {
                        start.linkTo(parent.start)
                        top.linkTo(text1.top)
                        bottom.linkTo(text1.bottom)
                    }
            ) {
                Icon(painter = if (categoryPostState is CategoryPostState.Expanded) painterResource(id = R.drawable.arrow_up)
                        else painterResource(id = R.drawable.arrow_down),
                    contentDescription = "categoryExpand",
                    tint = color_gray500,
                )
            }


            // 카테고리 이름
            Text(text = category.name,
                style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.constrainAs(text1) {
                    top.linkTo(parent.top)
                    start.linkTo(button.end, margin = 8.dp)
                    end.linkTo(row.start)
                    width = Dimension.fillToConstraints
                })


            // 편집 및 삭제
            Row(modifier = Modifier
                .constrainAs(row) {
                    start.linkTo(text1.end, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    top.linkTo(text1.top)
                    bottom.linkTo(text1.bottom)
                }) {
                Text(text = stringResource(id = R.string.category_edit),
                    style = MaterialTheme.typography.h4.copy(color = color_gray500),
                    modifier = Modifier
                        .clickable {
                            viewModel.checkEditable(category.name, category.name)
                            categoryEditDialogShown.value = true
                        }
                        .padding(8.dp))

                Text(text = stringResource(id = R.string.category_remove),
                    style = MaterialTheme.typography.h4.copy(color = color_gray500),
                    modifier = Modifier
                        .clickable { categoryDeleteDialogShown.value = true }
                        .padding(8.dp))
            }


            // 전시 기록 개수
            Text(
                text = "${category.postNum}${stringResource(id = R.string.category_exhibit_cnt)}",
                style = MaterialTheme.typography.h4.copy(color = color_gray500),
                modifier = Modifier.constrainAs(text2) {
                    start.linkTo(text1.start)
                    top.linkTo(text1.bottom, margin = 4.dp)
                },
                textAlign = TextAlign.Start
            )

        }
        if (categoryPostState is CategoryPostState.Expanded) {
            val data = categoryPostState.categoryPost.content
            // 카테고리에 담긴 전시 정보
            if (data.isNotEmpty()) {
                // 전시 정보가 존재 하는 경우
                Spacer(modifier = Modifier.height(24.dp))
                LazyRow(modifier = Modifier.padding(start = 48.dp, end = 16.dp)) {
                    items(data) { item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            AsyncImage(
                                model = item.mainImage,
                                error = painterResource(id = R.drawable.bg_image_placeholder),
                                placeholder = painterResource(id = R.drawable.bg_image_placeholder),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(4.5.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = item.name, style = MaterialTheme.typography.h4.copy(
                                    fontWeight = FontWeight.Medium, color = color_gray300
                                )
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(id = R.string.category_exhibit_empty),
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 카테고리 삭제 다이얼로그
        if (categoryDeleteDialogShown.value) {
            ConfirmDialog(title = stringResource(id = R.string.category_delete_title),
                subTitle = stringResource(id = R.string.category_delete_guide),
                onDismissRequest = { categoryDeleteDialogShown.value = false },
                onConfirm = {
                    viewModel.deleteCategory(category)
                    categoryDeleteDialogShown.value = false
                })
        }


        // 카테고리 편집 다이얼로그
        if (categoryEditDialogShown.value) {
            CategoryEditDialog(
                category = category.name,
                onEditCategory = { viewModel.editCategory(category, it) },
                onDismissRequest = { categoryEditDialogShown.value = false },
                checkEditable = { it1, it2 -> viewModel.checkEditable(it1, it2) },
                categoryState = viewModel.categoryState.collectAsState().value
            )
        }
    }
}

