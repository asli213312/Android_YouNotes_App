package com.example.android_younotes_app.presentation.thrash_screen

sealed class ThrashEvent {
    data object DeleteAll: ThrashEvent()
}