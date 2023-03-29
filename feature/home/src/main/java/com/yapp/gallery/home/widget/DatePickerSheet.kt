package com.yapp.gallery.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.common.theme.color_gray900
import com.yapp.gallery.common.theme.color_mainGreen
import com.yapp.gallery.common.theme.color_popUpBottom
import java.time.LocalDateTime

@Composable
fun DatePickerSheet(
    onDateSet : (String) -> Unit,
){
    // 오늘 날짜
    val dateTime = LocalDateTime.now()
    val todayYear = dateTime.year
    val todayMonth = dateTime.monthValue
    val todayDay = dateTime.dayOfMonth

    // YearList
    var yearList = (todayYear - 2 .. todayYear + 1).map { y -> "${y}년" }
    var monthList = (1 .. 12).map { m -> "${m}월" }


    var yearIndex = remember { mutableStateOf(yearList.size-2) }
    var monthIndex = remember { mutableStateOf(todayMonth - 1) }

    var dayLimit = remember {
        mutableStateOf(calculateDayLimit(yearIndex.value - 2 + todayYear, monthIndex.value + 1))
    }

    var dayList = remember{
        mutableStateOf((1..dayLimit.value).map { d -> "${d}일" })
    }

    var dayIndex = remember { mutableStateOf(todayDay - 1) }

    var changed = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(changed.value){
        if (changed.value){
            dayLimit.value = calculateDayLimit(yearIndex.value - 2 + todayYear, monthIndex.value + 1)
            if (dayLimit.value < dayIndex.value + 1){
                dayIndex.value = dayLimit.value - 1
            }
            dayList.value = (1..dayLimit.value).map { d -> "${d}일" }
            changed.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color_popUpBottom),
    ) {
        TextButton(onClick = { onDateSet("${yearIndex.value - 2 + todayYear}/${monthIndex.value + 1}/${dayIndex.value + 1}") },
            modifier = Modifier
                .align(Alignment.End)
                .padding(
                    top = 8.dp, end = 12.dp
                )
        ) {
            Text("완료", style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium))
        }
        Spacer(modifier = Modifier.height(20.dp))
        // 5 : 4 : 4 비율로 나누기
        Row(modifier = Modifier.fillMaxWidth()) {
            // 연 픽커
            Column(
                modifier = Modifier.weight(5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ListItemPicker(value = yearList[yearIndex.value],
                    onValueChange = {
                        yearIndex.value = yearList.indexOf(it)
                        changed.value = true
                    },
                    list = yearList,
                    dividersColor = Color(android.graphics.Color.TRANSPARENT),
                    textStyle = MaterialTheme.typography.h2.copy(
                        color = color_mainGreen, fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Divider(modifier = Modifier
                .padding(top = 36.dp)
                .height(60.dp)
                .width(0.8.dp), color = color_gray900)

            // 월 픽커
            Column(
                modifier = Modifier.weight(4f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ListItemPicker(value = monthList[monthIndex.value],
                    onValueChange = {
                        monthIndex.value = monthList.indexOf(it)
                        changed.value = true
                    },
                    list = monthList,
                    dividersColor = Color(android.graphics.Color.TRANSPARENT),
                    textStyle = MaterialTheme.typography.h2.copy(
                        color = color_mainGreen, fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Divider(modifier = Modifier
                .padding(top = 36.dp)
                .height(60.dp)
                .width(0.8.dp), color = color_gray900)

            // 일 피커
            Column(
                modifier = Modifier.weight(4f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ListItemPicker(value = dayList.value[dayIndex.value],
                    onValueChange = {dayIndex.value = dayList.value.indexOf(it)},
                    list = dayList.value,
                    dividersColor = Color(android.graphics.Color.TRANSPARENT),
                    textStyle = MaterialTheme.typography.h2.copy(
                        color = color_mainGreen, fontWeight = FontWeight.SemiBold
                    )
                )
            }
            
        }
        Spacer(modifier = Modifier.height(20.dp))
    }


}

fun calculateDayLimit(curYear: Int, curMonth: Int) : Int{
    // 현재 달이 2월인 경우
    return when(curMonth){
        2 -> {
            if (curYear % 4 == 0) {
                if (curYear % 100 == 0) {
                    if (curYear % 400 == 0) 29
                    // 100의 배수이지만 400의 배수가 아니면 평년
                    else 28
                } else
                    29
            } else 28
        }
        4, 6, 9, 11 -> 30
        else -> 31
    }

}

@Preview(showBackground = true)
@Composable
fun DatePickerSheetPreview() {
    GalleryTheme {
        DatePickerSheet(onDateSet = {})
    }
}
