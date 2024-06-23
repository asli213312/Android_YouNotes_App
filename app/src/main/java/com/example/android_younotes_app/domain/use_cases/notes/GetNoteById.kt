package com.example.android_younotes_app.domain.use_cases.notes

import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.NoteRepository

class GetNoteById(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? {
        return noteRepository.getNoteById(id)
    }
}