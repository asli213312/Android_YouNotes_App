package com.example.android_younotes.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.android_younotes_app.R

val bold = FontFamily(
    Font(R.font.comfortaa_bold)
)

val semiBold = FontFamily(
    Font(R.font.comfortaa_semi_bold)
)

val light = FontFamily(
    Font(R.font.comfortaa_light)
)

val medium = FontFamily(
    Font(R.font.comfortaa_medium)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = semiBold,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
        fontFamily = medium,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    )
)