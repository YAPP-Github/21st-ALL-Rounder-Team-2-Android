package com.yapp.gallery.home.widget.exhibit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.theme.color_gray700
import com.yapp.gallery.common.theme.color_white
import com.yapp.gallery.common.theme.pretendard
import com.yapp.gallery.home.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExhibitDate(
    exhibitDate : MutableState<String>,
    scope : CoroutineScope,
    focusManager: FocusManager,
    modalBottomSheetState : ModalBottomSheetState
){
    // 관람 날짜 고르기
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        Text(
            text = stringResource(id = R.string.exhibit_date),
            style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(12.dp))
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
            color = color_gray600,
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.8.dp
        )
    }
}