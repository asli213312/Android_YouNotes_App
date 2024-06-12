package com.example.android_younotes_app.domain.use_cases

import com.example.android_younotes_app.domain.models.NoteCategory
import com.example.android_younotes_app.domain.repository.NoteRepository

class BookmarkNote(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(noteId: Int, state: Boolean) {
        noteRepository.bookmarkNote(noteId, state)
    }
}