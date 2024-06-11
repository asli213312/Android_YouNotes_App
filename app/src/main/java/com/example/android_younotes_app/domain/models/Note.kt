package com.example.android_younotes_app.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val lastChanged: Long,
    val timeCreated: Long,
    val isPinned: String? = null,
    val tag: String? = null,
    @PrimaryKey
    val id: Int? = null
)

enum class NoteCategory {
    DEFAULT,
    BOOKMARKED,
    ARCHIVED
}

class InvalidNoteException(message: String) : Exception(message)