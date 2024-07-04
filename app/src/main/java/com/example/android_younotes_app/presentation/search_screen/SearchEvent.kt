package com.example.android_younotes_app.presentation.search_screen

sealed class SearchEvent {
    data class EnteredText(val text: String): SearchEvent()
}