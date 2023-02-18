package com.yapp.gallery.home.widget.exhibit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.theme.color_gray700
import com.yapp.gallery.common.theme.color_mainGreen
import com.yapp.gallery.common.widget.CategoryCreateDialog
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.home.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExhibitCategory(
    categoryList: List<CategoryItem>,
    focusManager: FocusManager,
    categorySelect: MutableState<Long>,
    addCategory: (String) -> Unit,
    checkCategory: (String) -> Unit,
    categoryState: BaseState<Boolean>
){
    // 카테고리 생성 다이얼로그
    val categoryDialogShown = remember { mutableStateOf(false) }

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
            if (categoryList.size.compareTo(5) == -1) {
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
                            tint = color_mainGreen,
                            modifier = Modifier.size(16.dp)
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
        if (categoryList.isEmpty()) {
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = stringResource(id = R.string.category_empty_guide),
                style = MaterialTheme.typography.h4.copy(color = color_gray700, lineHeight = 21.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(55.dp))
        } else {
            FlowRow{
                categoryList.forEach { item ->
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
            Spacer(modifier = Modifier.height(50.dp))
        }

    }

    // 카테고리 다이얼로그
    if (categoryDialogShown.value) {
        CategoryCreateDialog(
            onCreateCategory = {
                addCategory(it)
                categoryDialogShown.value = false
            },
            onDismissRequest = { categoryDialogShown.value = false },
            categoryState = categoryState,
            checkCategory = { checkCategory(it) }
        )
    }
}