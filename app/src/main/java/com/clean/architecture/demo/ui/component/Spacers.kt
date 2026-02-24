package com.clean.architecture.demo.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SpacerHeight(heightDp: Int) {
    Spacer(modifier = Modifier.height(heightDp.dp))
}


@Composable
fun SpacerWidth(heightDp: Int) {
    Spacer(modifier = Modifier.width(heightDp.dp))
}