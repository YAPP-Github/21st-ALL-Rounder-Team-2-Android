package com.yapp.gallery.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.yapp.gallery.R
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.common.theme.color_background
import com.yapp.gallery.common.theme.grey_929191

@Composable
fun SplashScreen()
{
    ConstraintLayout(
        modifier = Modifier.fillMaxSize().background(color = color_background)
    ) {
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
                bottom.linkTo(parent.bottom)
            }
        )

        Text(
            text = stringResource(id = R.string.app_slogan),
            style = MaterialTheme.typography.h3.copy(
                lineHeight = 24.sp, color = grey_929191),
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(slogan){
                top.linkTo(typo.bottom, margin = 30.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    GalleryTheme {
        SplashScreen()
    }
}