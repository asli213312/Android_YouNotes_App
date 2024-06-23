package com.example.android_younotes_app.presentation._global_components_.utils

import com.example.android_younotes_app.R

sealed class DrawerMenuItem(val icon: Int, val label: String) {
    data object Settings : DrawerMenuItem(R.drawable.vector_settings, "Settings")
    data object Notes : DrawerMenuItem(R.drawable.vector_notes, "All notes")
    data object Reminders : DrawerMenuItem(R.drawable.vector_reminders, "Reminders")
    data object Labels : DrawerMenuItem(R.drawable.vector_labels, "Labels")
    data object Thrash : DrawerMenuItem(R.drawable.vector_thrash, "Thrash")
    data object AccountSync : DrawerMenuItem(R.drawable.vector_sync, "Account and Sync")
}