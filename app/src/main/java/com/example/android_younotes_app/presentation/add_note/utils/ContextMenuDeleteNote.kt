package com.example.android_younotes_app.presentation.add_note.utils

import androidx.compose.ui.graphics.Color
import com.example.android_younotes_app.R
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.SelectableNoteRepository

sealed class ContextMenuDeleteNote(
    title: String,
    icon: Int,
    color: Color = Color.White
) : ContextMenuAbstract(title, icon, color) {

    data class Restore(var note: Note?): ContextMenuDeleteNote(
        title = "Restore",
        icon = R.drawable.vector_restore
    ), SelectableNoteRepository {
        override fun invoke(selectedNote: Note) {
            note = selectedNote
        }
    }
}