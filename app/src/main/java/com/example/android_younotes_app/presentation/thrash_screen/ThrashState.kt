package com.example.android_younotes_app.presentation.thrash_screen

import com.example.android_younotes_app.domain.models.Note

data class ThrashState(
    val deletedNotes: List<Note> = emptyList()
)
