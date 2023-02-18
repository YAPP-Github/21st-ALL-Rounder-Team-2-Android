package com.yapp.gallery.home.widget.exhibit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.theme.color_gray700
import com.yapp.gallery.common.theme.color_white
import com.yapp.gallery.home.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExhibitLink(
    exhibitLink: MutableState<String>,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
){
    // 웹 링크
    Column {
        Text(
            text = stringResource(id = R.string.exhibit_link),
            style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        BasicTextField(
            maxLines = 1,
            value = exhibitLink.value,
            onValueChange = { exhibitLink.value = it },
            textStyle = MaterialTheme.typography.h3,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Todo : 링크 임베드
                    focusManager.clearFocus()
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                ) {
                    if (exhibitLink.value.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.exhibit_link_hint),
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
                // Todo : 링크 임베드
                true
            },
            cursorBrush = SolidColor(color_white)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            color = color_gray600,
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.8.dp
        )
    }
}