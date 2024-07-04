package com.example.android_younotes_app.domain.use_cases.notes

import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.NoteRepository

class DeleteNote(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        noteRepository.deleteNote(note)
    }
}