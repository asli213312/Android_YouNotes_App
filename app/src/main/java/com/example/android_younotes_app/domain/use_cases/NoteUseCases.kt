package com.example.android_younotes_app.domain.use_cases

import com.example.android_younotes_app.domain.use_cases.AddNote
import com.example.android_younotes_app.domain.use_cases.BookmarkNote

data class NoteUseCases(
    val addNote: AddNote,
    val bookmarkNote: BookmarkNote,
    val getNotes: GetNotes
)