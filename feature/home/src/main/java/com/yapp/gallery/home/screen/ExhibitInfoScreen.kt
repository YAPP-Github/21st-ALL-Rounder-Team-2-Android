package com.yapp.gallery.home.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.flowlayout.FlowColumn
import com.google.accompanist.flowlayout.FlowRow
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.common.theme.*
import com.yapp.gallery.home.widget.CategoryDialog
import com.yapp.gallery.home.widget.DatePicker
import com.yapp.gallery.home.R
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun ExhibitInfoScreen(
    navController : NavHostController
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var exhibitName = rememberSaveable{ mutableStateOf("") }
//    var exhibitCategory = rememberSaveable { mutableStateOf("") }
    var exhibitDate = rememberSaveable { mutableStateOf("")}

    val interactionSource = remember { MutableInteractionSource() }

    // DropDownExpanded
    val dropDownExpanded = remember { mutableStateOf(false)}
    // DatePickerShown
    val datePickerShown = remember { mutableStateOf(false)}
    // CategoryDialogShown
    val categoryDialogShown = remember { mutableStateOf(false) }

    // rowSize 지정
    var rowSize = remember { mutableStateOf(Size.Zero) }


    // 카테고리 리스트
    val exhibitCategory = mutableListOf<String>()
    val categorySelect = rememberSaveable {
        mutableStateOf(-1)
    }

    // Bottom Sheet State
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Text(text = "테스트")
        }
    ) {
        Scaffold(
            topBar = {
                CenterTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    title = {
                        Text(text = stringResource(id = R.string.exhibit_title),
                            color = color_white,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) },
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
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.exhibit_temp),
                                color = color_gray400,
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                // 전시명 입력 부분
                Column {
                    Text(text = stringResource(id = R.string.exhibit_name),
                        fontFamily = pretendard, fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = color_white
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    BasicTextField(
                        maxLines = 1,
                        value = exhibitName.value,
                        onValueChange = { exhibitName.value = it },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = pretendard,
                            color = color_white
                        ),
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
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)) {
                                if (exhibitName.value.isEmpty()) {
                                    Text(text = stringResource(id = R.string.exhibit_name_hint),
                                        fontFamily = pretendard,
                                        color = color_gray700,
                                        fontSize = 16.sp)
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
                    Divider(color = color_gray900, modifier = Modifier.fillMaxWidth(), thickness = 0.8.dp)
                }

                Spacer(modifier = Modifier.height(48.dp))
                // 전시 카테고리 선택 부분
                Column {
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.exhibit_category), fontFamily = pretendard,
                            fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = color_white)
                        Spacer(modifier = Modifier.weight(1f))
                        CompositionLocalProvider(
                            LocalMinimumTouchTargetEnforcement provides false,
                        ) {
                            TextButton(onClick = {
                                categoryDialogShown.value = !categoryDialogShown.value
                                focusManager.clearFocus() },
                                contentPadding = PaddingValues()
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = color_mainGreen)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = "카테고리 만들기", fontFamily = pretendard,
                                    fontSize = 14.sp, fontWeight = FontWeight.Light, color = color_mainGreen)
                            }
                        }


                    }
                    // 버튼 클릭 되었을 때 DropDown 메뉴
//                ExhibitCategoryDropDownMenu(isExpanded = dropDownExpanded, onDropDownClick = {
//                    exhibitCategory.add(it)
//                    dropDownExpanded.value = false
//                }, rowSize = rowSize.value.width,
//                    interactionSource = interactionSource,
//                    onCreateCategoryClick = {
//                        dropDownExpanded.value = false
//                        categoryDialogShown.value = !categoryDialogShown.value
//                    }
//                )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(modifier = Modifier.defaultMinSize(minHeight = 60.dp),
                    ){
                        exhibitCategory.forEachIndexed { index, item ->
                            Surface(shape = RoundedCornerShape(71.dp),
                                onClick = {
                                    if (categorySelect.value == index) categorySelect.value = -1
                                    else categorySelect.value = index
                                },
                                color = if (categorySelect.value == index) MaterialTheme.colors.secondary
                                else MaterialTheme.colors.background,
                                contentColor = if (categorySelect.value == index) Color(0xFF282828)
                                else MaterialTheme.colors.secondary,
                                border = BorderStroke(1.dp, color = MaterialTheme.colors.secondary)

                            ) {
                                Text(text = item, fontFamily = pretendard, fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                )
                            }

//                        Button(
//                            onClick = { if (categorySelect.value == index) categorySelect.value = -1
//                                else categorySelect.value = index
//                            }, shape = RoundedCornerShape(71.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                backgroundColor = MaterialTheme.colors.secondary,
//                                contentColor = Color(0xFF282828),
//                                disabledBackgroundColor = MaterialTheme.colors.background,
//                                disabledContentColor = MaterialTheme.colors.secondary
//                            ),
//                            enabled = categorySelect.value == index
//                        ) {
//                            Text(text = item, fontFamily = pretendard, fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp, color = Color.White
//                            )
//                        }
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(42.dp))
                // 관람 날짜 고르기
                Column {
                    Text(
                        text = stringResource(id = R.string.exhibit_date),
                        fontFamily = pretendard,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = color_white
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .clickable(
                                // ripple color 없애기
                                indication = null,
                                interactionSource = interactionSource
                            ) {
                                datePickerShown.value = !datePickerShown.value
                                focusManager.clearFocus()
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
                    Divider(color = color_gray900, modifier = Modifier.fillMaxWidth(), thickness = 0.8.dp)
                }

                Spacer(modifier = Modifier.weight(1f))
                // 전시 기록장 생성하기 버튼
                Button(onClick = { },
                    modifier = Modifier.fillMaxWidth(),
//                enabled = exhibitName.value.isNotEmpty() && exhibitCategory.isNotEmpty() && exhibitDate.value.isNotEmpty()
                ) {
                    Text(text = stringResource(id = R.string.exhibit_create_btn), fontFamily = pretendard, fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold, color = color_black,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(63.dp))
            }

            if (datePickerShown.value){
                DatePicker(onDateSelected = {
                    exhibitDate.value = it.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")).also { d -> Log.e("datePick", d) }
                }, onDismissRequest = {
                    datePickerShown.value = false
                })
            }
            if (categoryDialogShown.value){
                CategoryDialog(onCreateCategory = {
                    exhibitCategory.add(it)
                    categoryDialogShown.value = false },
                    onDismissRequest = {categoryDialogShown.value = false})
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun ExhibitScreenPreview(){
    ExhibitInfoScreen(rememberNavController())
}

val categoryList = listOf("카테고리 예시 01", "카테고리 예시 02", "카테고리 예시 03")

@Composable
fun ExhibitCategoryDropDownMenu(
    isExpanded: MutableState<Boolean>,
    onDropDownClick : (String) -> Unit,
    rowSize: Float,
    interactionSource : MutableInteractionSource,
    onCreateCategoryClick : () -> Unit
){
    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
        DropdownMenu(
            offset = DpOffset(0.dp, 12.dp),
            modifier = Modifier.width(with(LocalDensity.current) { rowSize.toDp() }),
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false }
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            categoryList.forEach {
                Column(modifier = Modifier.fillMaxWidth()) {
                    DropdownMenuItem(onClick = { onDropDownClick(it) },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(text = it, fontFamily = pretendard, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                    }
                    Divider(color = grey_bdbdbd, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), thickness = 0.4.dp)
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 21.dp)
                    .padding(top = 20.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .clickable(
                        // ripple color 없애기
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onCreateCategoryClick
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(32.dp),
                    border = BorderStroke(0.8.dp, Color.Black)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(id = R.string.exhibit_category_create_btn), fontSize = 16.sp, fontFamily = pretendard, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}