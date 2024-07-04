package com.example.android_younotes_app.presentation.notes_screen

import com.example.android_younotes_app.domain.models.Note

data class NotesState(
    val canLoadMedia: Boolean = false,
    val notes: List<Note> = emptyList()
)
