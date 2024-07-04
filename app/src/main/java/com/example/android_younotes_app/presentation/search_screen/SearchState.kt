package com.example.android_younotes_app.presentation.search_screen

import com.example.android_younotes_app.domain.models.Note

data class SearchState(
    val foundNotes: List<Note> = emptyList(),
    val allNotes: List<Note> = emptyList()
)
