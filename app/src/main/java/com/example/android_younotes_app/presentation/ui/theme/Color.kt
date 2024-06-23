package com.example.android_younotes_app.presentation.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Background = Color(0xFF191717)
val Primary = Color(0xA61E1E1E)
val Stroke = Color(0x31646464)

val ThemeGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF43CBFF),
        Color(0xFF9708CC)
    )
)

val BlackGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF2E2E2E),
        Color(0xFF131313),
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)

val RedGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xB3FF5980),
        Color(0xB34E1934)
    ),
    start = Offset.Zero,
    end = Offset.Infinite,
    tileMode = TileMode.Mirror
)

val GreenGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xB323EE2C),
        Color(0xB32A5822)
    ),
    start = Offset.Zero,
    end = Offset.Infinite,
    tileMode = TileMode.Mirror
)

val ClearGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x002E2E2E),
        Color(0x00131313),
    ),
)