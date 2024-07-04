package com.example.android_younotes_app.domain.repository

import com.example.android_younotes_app.domain.models.Note

interface SelectableNoteRepository {

    fun invoke(selectedNote: Note)
}