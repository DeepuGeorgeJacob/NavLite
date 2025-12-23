package com.navigo.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavLiteScaffold(
    modifier: Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(modifier = modifier, topBar = topBar) {
        content(modifier.padding(it))
    }

}