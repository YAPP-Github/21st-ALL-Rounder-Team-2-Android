package com.yapp.gallery.home.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.home.R
import com.yapp.gallery.home.widget.CategoryDialog
import com.yapp.gallery.home.widget.DatePickerSheet
import com.yapp.gallery.home.widget.RecordMenuDialog
import com.yapp.gallery.home.widget.TempStorageDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun ExhibitRecordScreen(
    navController: NavHostController,
    viewModel: ExhibitInfoViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val exhibitName = rememberSaveable { mutableStateOf("") }
    val exhibitDate = rememberSaveable { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }

    val categoryDialogShown = remember { mutableStateOf(false) }
    val recordMenuDialogShown = remember { mutableStateOf(false) }
    val tempStorageDialogShown = remember { mutableStateOf(false) }

    // 카테고리 리스트
    val categorySelect = rememberSaveable {
        mutableStateOf(-1L)
    }

    // Bottom Sheet State
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()


    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            DatePickerSheet(onDateSet = {
                exhibitDate.value = it
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
                    navigationIcon = if (navController.previousBackStackEntry != null) {
                        {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    } else {
                        null
                    },
                    actions = {
                        TextButton(onClick = { tempStorageDialogShown.value = true }) {
                            Text(
                                text = stringResource(id = R.string.exhibit_temp),
                                style = MaterialTheme.typography.h3.copy(
                                    fontWeight = FontWeight.Medium, color = color_gray400
                                ),
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                // 전시명 입력 부분
                Column {
                    Text(
                        text = stringResource(id = R.string.exhibit_name),
                        style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    BasicTextField(
                        maxLines = 1,
                        value = exhibitName.value,
                        onValueChange = { if (it.length <= 20) exhibitName.value = it },
                        textStyle = MaterialTheme.typography.h3,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                            ) {
                                if (exhibitName.value.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.exhibit_name_hint),
                                        style = MaterialTheme.typography.h3.copy(color = color_gray700)
                                    )
                                }
                            }
                            innerTextField()
                        },
                        modifier = Modifier.onKeyEvent { keyEvent ->
                            if (keyEvent.key != Key.Enter || keyEvent.key != Key.SystemNavigationDown) return@onKeyEvent false
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            true
                        },
                        cursorBrush = SolidColor(color_white)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        color = color_gray900,
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.8.dp
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                // 전시 카테고리 선택 부분
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.exhibit_category), style =
                            MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (viewModel.categoryList.size.compareTo(5) == -1) {
                            CompositionLocalProvider(
                                LocalMinimumTouchTargetEnforcement provides false,
                            ) {
                                TextButton(
                                    onClick = {
                                        categoryDialogShown.value = !categoryDialogShown.value
                                        focusManager.clearFocus()
                                    },
                                    contentPadding = PaddingValues()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = color_mainGreen
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "카테고리 만들기", style = MaterialTheme.typography.h4.copy(
                                            fontWeight = FontWeight.Medium, color = color_mainGreen
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    if (viewModel.categoryList.isEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.category_empty_guide),
                            style = MaterialTheme.typography.h4.copy(color = color_gray700, lineHeight = 21.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    } else {
                        FlowRow(
                            modifier = Modifier.defaultMinSize(minHeight = 60.dp),
                        ) {
                            viewModel.categoryList.forEach { item ->
                                Surface(
                                    shape = RoundedCornerShape(71.dp),
                                    onClick = {
                                        if (categorySelect.value == item.id) categorySelect.value =
                                            -1
                                        else categorySelect.value = item.id
                                    },
                                    color = if (categorySelect.value == item.id) MaterialTheme.colors.secondary
                                    else MaterialTheme.colors.background,
                                    border = BorderStroke(
                                        1.dp,
                                        color = MaterialTheme.colors.secondary
                                    )

                                ) {
                                    Text(
                                        text = item.name, style = MaterialTheme.typography.h4.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (categorySelect.value == item.id) Color(
                                                0xFF282828
                                            )
                                            else MaterialTheme.colors.secondary
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 10.dp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.height(44.dp))
                // 관람 날짜 고르기
                Column {
                    Text(
                        text = stringResource(id = R.string.exhibit_date),
                        style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .clickable(
                                // ripple color 없애기
                                indication = null,
                                interactionSource = interactionSource
                            ) {
                                scope.launch {
                                    focusManager.clearFocus()
                                    modalBottomSheetState.show()
                                }
                            }
                    ) {
                        if (exhibitDate.value.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.exhibit_date_hint),
                                fontFamily = pretendard,
                                color = color_gray700,
                                fontSize = 16.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Text(
                                text = exhibitDate.value, fontFamily = pretendard, fontSize = 16.sp,
                                maxLines = 1, modifier = Modifier.weight(1f),
                                color = color_white
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        color = color_gray900,
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.8.dp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                // 전시 기록장 생성하기 버튼
                Button(
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = color_gray600,
                        disabledContentColor = color_gray900,
                        backgroundColor = color_mainGreen,
                        contentColor = color_black
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    enabled = exhibitName.value.isNotEmpty() && categorySelect.value != -1L && exhibitDate.value.isNotEmpty()
                ) {
                    Text(
                        text = stringResource(id = R.string.exhibit_create_btn),
                        modifier = Modifier.padding(vertical = 12.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = pretendard
                    )
                }
                Spacer(modifier = Modifier.height(53.dp))
            }

            // 임시 보관함 다이얼로그
            if (tempStorageDialogShown.value) {
                TempStorageDialog(
                    onDismissRequest = { tempStorageDialogShown.value = false },
                    viewModel = viewModel
                )
            }

            // 카테고리 다이얼로그
            if (categoryDialogShown.value) {
                CategoryDialog(
                    onCreateCategory = {
                        viewModel.addCategory(it)
                        categoryDialogShown.value = false
                    },
                    onDismissRequest = { categoryDialogShown.value = false },
                    viewModel = viewModel
                )
            }

            // 전시 기록 시작 다이얼로그
            if (recordMenuDialogShown.value) {
                RecordMenuDialog(
                    onCameraClick = {
                        // Todo : 카메라 촬영으로 이동
//                        viewModel.createRecord(exhibitName.value, categorySelect.value, changeDateFormat(exhibitDate.value) )
                    },
                    onGalleryClick = {},
                    onDismissRequest = { recordMenuDialogShown.value = false }
                )
            }
        }
    }

}

fun changeDateFormat(postDate: String): String {
    var dateList = postDate.split('/')
    return String.format(
        "%4d-%02d-%02d",
        dateList[0].toInt(),
        dateList[1].toInt(),
        dateList[2].toInt()
    )
}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
