package com.example.android_younotes_app.domain.use_cases

import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(
    private val noteRepository: NoteRepository
) {

    operator fun invoke(): Flow<List<Note>> {
        return noteRepository.getNotes()
    }
}