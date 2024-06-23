package com.example.android_younotes_app.presentation.settings.components

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android_younotes_app.presentation.ui.theme.Stroke
import com.example.android_younotes_app.presentation.ui.theme.ThemeGradient

@Composable
fun GradientSwitch(
    modifier: Modifier,
    size: Dp,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val thumbGradient = ThemeGradient
    val trackGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF262626),
            Color(0xFF1E1E1E)
        )
    )

    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) 9.dp else (-9).dp, label = ""
    )

    Box(
        modifier = Modifier
            .width(size)
            .height(size - 20.dp)
            .fillMaxWidth()
            .background(
                brush = trackGradient,
                shape = RoundedCornerShape(15.dp)
            )
            .clickable {
                Log.d("GradientSwitch", "Clicked on thumb with state: $isChecked")
                onCheckedChange(!isChecked)
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
        ) {
            drawRoundRect(
                brush = trackGradient,
                cornerRadius = CornerRadius(15.dp.toPx(), 15.dp.toPx()),
            )
            drawRoundRect(
                brush =
                    if (isChecked)
                        SolidColor(Color.Gray)
                    else
                        SolidColor(Color.Gray.copy(0.5f)),
                cornerRadius = CornerRadius(15.dp.toPx(), 15.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )
        }
        Box(
            modifier = Modifier
                .size(size - 24.dp)
                .offset(x = thumbOffset)
                .background(brush = thumbGradient, CircleShape)
        )
    }
}