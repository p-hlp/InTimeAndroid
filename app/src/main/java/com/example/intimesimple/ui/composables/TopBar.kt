package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String
){
    TopAppBar(
        title = {
            Text(title)
        }
    )
}


@Preview
@Composable
fun TopBarPreview(){
    MaterialTheme {
        TopBar(
            title = "TestTitle"
        )
    }
}


