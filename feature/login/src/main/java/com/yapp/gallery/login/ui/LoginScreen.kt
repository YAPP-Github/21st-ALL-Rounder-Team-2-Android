package com.yapp.gallery.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.common.theme.color_gray300
import com.yapp.gallery.common.theme.color_mainBlue
import com.yapp.gallery.common.theme.grey_d5d5d5
import com.yapp.gallery.login.R
import com.yapp.gallery.login.ui.LoginContract.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LoginScaffold(
        isLoading = isLoading,
        onGoogleLogin = { viewModel.setEvent(LoginEvent.OnGoogleLogin) },
        onKakaoLogin = { viewModel.setEvent(LoginEvent.OnKakaoLogin) },
        onNaverLogin = { viewModel.setEvent(LoginEvent.OnNaverLogin) }
    )
}

@Composable
private fun LoginScaffold(
    isLoading: Boolean,
    onGoogleLogin: () -> Unit,
    onNaverLogin: () -> Unit,
    onKakaoLogin: () -> Unit
){
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (logo, typo, slogan) = createRefs()
                Image(
                    painterResource(id = R.drawable.ic_logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(typo.top, margin = 34.dp)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_typo),
                    contentDescription = "typo",
                    modifier = Modifier.constrainAs(typo){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 40.dp)
                    }
                )

                Text(
                    text = stringResource(id = R.string.service_slogan),
                    style = MaterialTheme.typography.h3.copy(
                        lineHeight = 24.sp, color = grey_d5d5d5),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.constrainAs(slogan){
                        top.linkTo(typo.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Row {
                    Image(painter = painterResource(id = R.drawable.ic_naver_login),
                        contentDescription = "kakao",
                        modifier = Modifier
                            .size(72.dp)
                            .clickable(onClick = onNaverLogin)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(painter = painterResource(id = R.drawable.ic_kakao_login),
                        contentDescription = "kakao",
                        modifier = Modifier
                            .size(72.dp)
                            .clickable(onClick = onKakaoLogin)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(painter = painterResource(id = R.drawable.ic_google_login),
                        contentDescription = "google",
                        modifier = Modifier
                            .size(72.dp)
                            .clickable(onClick = onGoogleLogin)
                    )
                }
                Spacer(modifier = Modifier.height(120.dp))
            }

            // 로딩 스크린
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = color_gray300.copy(alpha = 0.2f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = color_mainBlue
                    )
                }
            }


        }
    }
}

//fun Modifier.modifyIf(condition: Boolean, modify: Modifier.() -> Modifier) =
//    if (condition) modify() else this

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    GalleryTheme {
        LoginScaffold(
            isLoading = false,
            onGoogleLogin = {  },
            onNaverLogin = {  },
            onKakaoLogin = { }
        )
    }
}


