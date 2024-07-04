package com.example.android_younotes_app.presentation.notes_screen

sealed class NotesEvent {
    data class EnteredSearchText(val value: String): NotesEvent()
}