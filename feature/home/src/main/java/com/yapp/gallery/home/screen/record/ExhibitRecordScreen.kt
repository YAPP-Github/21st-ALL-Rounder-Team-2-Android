package com.yapp.gallery.home.screen.record

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
import com.yapp.gallery.home.R
import com.yapp.gallery.home.widget.DatePickerSheet
import com.yapp.gallery.home.widget.RecordMenuDialog
import com.yapp.gallery.home.widget.exhibit.ExhibitCategory
import com.yapp.gallery.home.widget.exhibit.ExhibitDate
import com.yapp.gallery.home.widget.exhibit.ExhibitLink
import com.yapp.gallery.home.widget.exhibit.ExhibitRecordName
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun ExhibitRecordScreen(
    navigateToCamera: () -> Unit,
    navigateToGallery: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: ExhibitRecordViewModel = hiltViewModel(),
) {
    // 키보드 포커스
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // 카테고리 State
    val categoryState: BaseState<Boolean> by viewModel.categoryState.collectAsState()
    val exhibitRecordState : ExhibitRecordState? by viewModel.recordScreenState.collectAsState()

    // Bottom Sheet State
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // 스크롤 상태
    val scrollState = rememberScrollState()
    // ScaffoldState
    val scaffoldState = rememberScaffoldState()

    // 기록 방법 다이얼로그
    val recordMenuDialogShown = remember { mutableStateOf(false) }
    // 임시 저장 다이얼로그
    val tempPostDialogShown = remember { mutableStateOf(false) }


    // 스크린 상태
    LaunchedEffect(exhibitRecordState){
        when(exhibitRecordState){
            is ExhibitRecordState.Response -> {
                tempPostDialogShown.value = true
            }
            is ExhibitRecordState.Delete -> {
                scope.launch {
                    val res = scaffoldState.snackbarHostState.showSnackbar(
                        message = "임시 보관된 전시를 삭제하였습니다.",
                        actionLabel = "취소",
                        duration = SnackbarDuration.Short
                    )
                    when(res){
                        SnackbarResult.Dismissed -> {viewModel.undoDelete(false)}
                        SnackbarResult.ActionPerformed -> {viewModel.undoDelete(true)}
                    }
                }
            }
            else -> {}
        }
    }

    // 임시 포스트 다이얼로그
    if (tempPostDialogShown.value) {
        ConfirmDialog(
            title = stringResource(id = R.string.temp_post_title),
            subTitle = stringResource(id = R.string.temp_post_guide),
            onDismissRequest = {
                viewModel.setContinuousDelete(false)
                tempPostDialogShown.value = false
            },
            onConfirm = {
                viewModel.setContinuousDelete(true)
                tempPostDialogShown.value = false},
            important = true
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
    ) {
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
                            text = stringResource(id = R.string.exhibit_title),
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
                        recordMenuDialogShown.value = true
                    },
                    enabled = viewModel.exhibitName.value.isNotEmpty() &&
                            viewModel.categorySelect.value != -1L &&
                            viewModel.exhibitDate.value.isNotEmpty()
                ) {
                    Text(
                        text = if (exhibitRecordState is ExhibitRecordState.Continuous) stringResource(id = R.string.exhibit_crate_continuous_btn)
                            else stringResource(id = R.string.exhibit_create_btn),
                        modifier = Modifier.padding(vertical = 12.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = pretendard
                    )
                }
            }

        }

        // 전시 기록 시작 다이얼로그
        if (recordMenuDialogShown.value) {
            RecordMenuDialog(
                onCameraClick = {
                    // Todo : 카메라 모듈로 이동
//                    viewModel.createRecord(
//                        exhibitName.value, categorySelect.value,
//                        exhibitDate.value, exhibitLink.value.ifEmpty { null })
                    navigateToCamera()
                    recordMenuDialogShown.value = false },
                onGalleryClick = {
                    // Todo : 저장소 모듈로 이동
                    viewModel.createOrUpdateRecord()
                    navigateToGallery()
                    recordMenuDialogShown.value = false },
                onDismissRequest = { recordMenuDialogShown.value = false }
            )
        }
    }

}