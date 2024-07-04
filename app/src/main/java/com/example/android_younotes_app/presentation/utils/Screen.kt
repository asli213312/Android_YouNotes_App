package com.example.android_younotes_app.presentation.utils

sealed class Screen(val route: String) {
    data object NotesScreen: Screen("notes_screen")
    data object AddNoteScreen: Screen("add_note_screen")
    data object SettingsScreen: Screen("settings_screen")
    data object SearchScreen: Screen("search_screen")
    data object ThrashScreen: Screen("thrash_screen")
}