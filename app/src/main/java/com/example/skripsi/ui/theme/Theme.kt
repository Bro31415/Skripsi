package com.example.skripsi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}