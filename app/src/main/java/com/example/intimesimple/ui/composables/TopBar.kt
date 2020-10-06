package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.ui.theme.INTimeTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String
){
    TopAppBar(
        title = {
            Text(
                text = title.toUpperCase()
            )
        }
    )
}


@Preview
@Composable
fun TopBarPreview(){
    INTimeTheme {
        TopBar(
            title = "TestTitle"
        )
    }
}


