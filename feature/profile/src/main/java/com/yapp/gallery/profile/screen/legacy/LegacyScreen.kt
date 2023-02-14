package com.yapp.gallery.profile.screen.legacy

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.gallery.common.theme.color_gray600
import com.yapp.gallery.common.theme.color_gray900
import com.yapp.gallery.common.widget.CenterTopAppBar
import com.yapp.gallery.profile.R

@Composable
fun LegacyScreen(
    popBackStack : () -> Unit,
    navigateToWebPage : (Int) -> Unit
){
    Scaffold(
       topBar = {
           CenterTopAppBar(
               modifier = Modifier.fillMaxWidth(),
               backgroundColor = Color.Transparent,
               elevation = 0.dp,
               title = {
                   Text(
                       text = stringResource(id = R.string.legacy_appbar_title),
                       style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold),
                       textAlign = TextAlign.Center,
                       modifier = Modifier.fillMaxWidth()
                   )
               },
               navigationIcon = {
                   IconButton(onClick = popBackStack) {
                       Icon(
                           imageVector = Icons.Filled.ArrowBack,
                           contentDescription = "Back"
                       )
                   }
               }
           )
       }
    ) {paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
        ) {
            LegacyTile(
                title = stringResource(id = R.string.legacy_service),
                onClick = { navigateToWebPage(R.string.legacy_service_link) })
            Divider(color = color_gray900, thickness = 0.4.dp, modifier = Modifier.fillMaxWidth())
            LegacyTile(
                title = stringResource(id = R.string.legacy_privacy),
                onClick = { navigateToWebPage(R.string.legacy_privacy_link)})
            Divider(color = color_gray900, thickness = 0.4.dp, modifier = Modifier.fillMaxWidth())

        }
    }
}

@Composable
private fun LegacyTile(
    title : String,
    onClick : () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium))
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onClick, modifier = Modifier.size(20.dp)) {
            Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LegacyTilePreview(){
    LegacyTile(title = "test", onClick = {})
}