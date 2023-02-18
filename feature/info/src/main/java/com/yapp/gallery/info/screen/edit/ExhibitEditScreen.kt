package com.yapp.gallery.info.screen.edit

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.common.widget.ConfirmDialog
<<<<<<< HEAD
=======
import com.yapp.gallery.home.R
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
import com.yapp.gallery.home.widget.DatePickerSheet
import com.yapp.gallery.home.widget.exhibit.ExhibitCategory
import com.yapp.gallery.home.widget.exhibit.ExhibitDate
import com.yapp.gallery.home.widget.exhibit.ExhibitLink
import com.yapp.gallery.home.widget.exhibit.ExhibitRecordName
<<<<<<< HEAD
import com.yapp.gallery.info.R
=======
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ExhibitEditScreen(
    popBackStack: () -> Unit,
<<<<<<< HEAD
    navigateToHome: () -> Unit,
=======
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
    viewModel: ExhibitEditViewModel = hiltViewModel(),
    context: Context = LocalContext.current
){
    // 키보드 포커스
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // 카테고리 State
    val categoryState: BaseState<Boolean> by viewModel.categoryState.collectAsState()

    // Bottom Sheet State
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // 스크롤 상태
    val scrollState = rememberScrollState()
    // ScaffoldState
    val scaffoldState = rememberScaffoldState()

    val exhibitDeleteDialogShown = remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.errors){
        viewModel.errors.collect{
            scaffoldState.snackbarHostState.showSnackbar(
                it.asString(context)
            )
        }
    }

    LaunchedEffect(viewModel.editState){
        viewModel.editState.collectLatest {
            when(it){
                is ExhibitEditState.Delete -> {
<<<<<<< HEAD
                    navigateToHome()
=======
                    // Todo : 홈화면 이동
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
                }
                is ExhibitEditState.Update -> {
                    // Todo : 이전 화면 이동 및 업데이트
                }
                is ExhibitEditState.Error -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        it.message.asString(context)
                    )
                }
                else -> {}
            }
        }
    }

    // 전시 정보 삭제 다이얼로그
    if (exhibitDeleteDialogShown.value){
        ConfirmDialog(
            title = stringResource(id = R.string.exhibit_delete_dialog_title),
            subTitle = stringResource(id = R.string.exhibit_delete_dialog_guide),
            onDismissRequest = { exhibitDeleteDialogShown.value = false },
            onConfirm = { viewModel.deleteRemotePost() }
        )
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            DatePickerSheet(onDateSet = {
                viewModel.exhibitDate.value = it
                scope.launch {
                    modalBottomSheetState.hide()
                }
            })
        },
        scrimColor = Color.Transparent,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetBackgroundColor = color_popUpBottom
    ){
        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = {
                SnackbarHost(it) { data ->
                    Snackbar(
                        snackbarData = data,
                        actionColor = color_purple500,
                        backgroundColor = Color.White,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            },
            topBar = {
                CenterTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    title = {
                        Text(
<<<<<<< HEAD
                            text = stringResource(id = R.string.exhibit_edit_appbar_title),
=======
                            text = stringResource(id = R.string.exhibit_title),
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
                            style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { exhibitDeleteDialogShown.value = true },
                        ) {
                            Text(
                                text = "전시 삭제",
                                style = MaterialTheme.typography.h3.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                )
            },
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(48.dp))

                    ExhibitRecordName(
                        name = viewModel.exhibitName,
                        focusManager = focusManager,
                        focusRequester = focusRequester
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    ExhibitCategory(
                        categoryList = viewModel.categoryList,
                        focusManager = focusManager,
                        categorySelect = viewModel.categorySelect,
                        addCategory = { viewModel.addCategory(it)},
                        checkCategory = { viewModel.checkCategory(it)},
                        categoryState = categoryState
                    )

                    ExhibitDate(
                        exhibitDate = viewModel.exhibitDate,
                        scope = scope,
                        focusManager = focusManager,
                        modalBottomSheetState = modalBottomSheetState
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    ExhibitLink(exhibitLink = viewModel.exhibitLink,
                        focusManager = focusManager,
                        focusRequester = focusRequester
                    )
                }
                // 하단 버튼
                Button(
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = color_gray600,
                        disabledContentColor = color_gray900,
                        backgroundColor = color_mainGreen,
                        contentColor = color_black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 53.dp),
                    onClick = {
                        viewModel.updateRemotePost()
                    },
                    enabled = viewModel.exhibitName.value.isNotEmpty() &&
                            viewModel.categorySelect.value != -1L &&
                            viewModel.exhibitDate.value.isNotEmpty()
                ) {
                    Text(
<<<<<<< HEAD
                        text = stringResource(id = R.string.exhibit_edit_update_btn),
=======
                        text = "수정 완료",
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
                        modifier = Modifier.padding(vertical = 12.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = pretendard
                    )
                }
            }
        }

    }
}