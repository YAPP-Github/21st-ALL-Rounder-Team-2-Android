package com.yapp.gallery.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.gallery.common.theme.pretendard

@Composable
fun LoginScreen(
    googleLogin : () -> Unit
){
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(
                            RoundedCornerShape(9.dp)
                        )
                        .background(color = Color.LightGray),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "LOGO")
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "서비스 이름", fontFamily = pretendard, fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "서비스 슬로건 슬로건", fontFamily = pretendard, fontSize = 16.sp)
                // Todo : 소셜 로그인 버튼들

            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Row {
                    Image(painter = painterResource(id = R.drawable.ic_google_login), contentDescription = "google",
                        modifier = Modifier
                            .size(72.dp)
                            .clickable(onClick = googleLogin)
                    )
                }
                Spacer(modifier = Modifier.height(120.dp))
            }

        }
    }
}