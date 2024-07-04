package com.example.android_younotes_app.presentation.add_note.utils

import androidx.compose.ui.graphics.Color
import com.example.android_younotes_app.R
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.SelectableNoteRepository

sealed class ContextMenuAddNote(
    title: String,
    icon: Int,
    color: Color = Color.White
) : ContextMenuAbstract(title, icon, color) {

    data class DeleteInThrash(var note: Note?) : ContextMenuAddNote(
        title = "Delete",
        icon = R.drawable.vector_thrash
    ), SelectableNoteRepository {
        override fun invoke(selectedNote: Note) {
            note = selectedNote
        }
    }

    data object SelectColor : ContextMenuAddNote(
        title = "Select color",
        icon = R.drawable.vector_select_color
    )

    data class Duplicate(var note: Note?) : ContextMenuAddNote(
        title = "Duplicate",
        icon = R.drawable.vector_duplicate
    ), SelectableNoteRepository {
        override fun invoke(selectedNote: Note) {
            note = selectedNote
        }
    }
}