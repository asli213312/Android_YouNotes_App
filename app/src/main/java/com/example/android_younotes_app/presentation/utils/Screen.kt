package com.example.android_younotes_app.presentation.utils

sealed class Screen(val route: String) {
    data object NotesScreen: Screen("notes_screen")
    data object AddNoteScreen: Screen("add_note_screen")
}