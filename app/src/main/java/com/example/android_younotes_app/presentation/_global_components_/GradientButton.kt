package com.example.android_younotes_app.presentation._global_components_

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    brush: Brush,
    shape: Shape = RoundedCornerShape(0.dp),
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    Box(
        modifier = modifier
            .background(
                brush = brush,
                shape = shape
            )
            .width(40.dp)
            .height(20.dp)
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}