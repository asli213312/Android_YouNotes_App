package com.example.android_younotes_app.domain.use_cases.notes

import com.example.android_younotes_app.domain.repository.NoteRepository

class DeleteNoteInThrash(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(noteId: Int, state: Boolean) {
        noteRepository.deleteNoteInThrash(noteId, state)
    }
}