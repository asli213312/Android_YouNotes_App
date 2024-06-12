package com.example.android_younotes_app.data.repository

import com.example.android_younotes_app.data.data_source.NoteDao
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.models.NoteCategory
import com.example.android_younotes_app.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao
)
    : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun changeCategoryNoteById(id: Int, category: NoteCategory) {

    }

    override suspend fun bookmarkNote(id: Int, state: Boolean) {
        noteDao.bookmarkNote(id, state)
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}