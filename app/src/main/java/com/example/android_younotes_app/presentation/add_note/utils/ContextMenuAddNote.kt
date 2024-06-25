package com.example.android_younotes_app.presentation.add_note.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.android_younotes_app.R

sealed class ContextMenuAddNote(
    val title: String,
    val icon: Int,
    val color: Color = Color.White
) {
    data object Delete : ContextMenuAddNote(
        "Delete", R.drawable.vector_thrash
    )
    data object SelectColor : ContextMenuAddNote(
        "Select color", R.drawable.vector_select_color
    )
    data object Duplicate : ContextMenuAddNote(
        "Duplicate", R.drawable.vector_duplicate
    )
}