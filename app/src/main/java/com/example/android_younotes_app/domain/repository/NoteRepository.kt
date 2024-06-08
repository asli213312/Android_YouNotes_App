package com.example.android_younotes_app.domain.repository

import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.models.NoteCategory
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun changeCategoryNoteById(id: Int, category: NoteCategory)

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}