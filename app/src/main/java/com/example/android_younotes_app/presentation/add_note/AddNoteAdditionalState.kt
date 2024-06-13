package com.example.android_younotes_app.presentation.add_note

import androidx.compose.ui.graphics.Brush
import com.example.android_younotes_app.domain.models.NoteGradient

data class AddNoteAdditionalState(
    val lastChanged: Long? = null,
    val timeCreated: Long? = null,
    val noteTag: String? = null,
    var backgroundGradient: NoteGradient? = null,
    val backgroundImagePath: String? = null,
    val previewGradient: NoteGradient? = null,
    val previewImagePath: String? = null
)
