package com.example.android_younotes_app.presentation.add_note

data class NoteTextFieldState(
    val query: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = false
)
