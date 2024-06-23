package com.example.android_younotes_app.domain.use_cases.notes

import com.example.android_younotes_app.domain.models.InvalidNoteException
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val noteRepository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank())
            throw InvalidNoteException("The title of the note can't be empty.")
        if (note.content.isBlank())
            throw InvalidNoteException("The content of the note can't be empty.")

        noteRepository.insertNote(note)
    }
}