package com.example.android_younotes_app.domain.use_cases.notes

data class NoteUseCases(
    val addNote: AddNote,
    val bookmarkNote: BookmarkNote,
    val getNotes: GetNotes,
    val getNoteById: GetNoteById,
    val deleteNoteInThrash: DeleteNoteInThrash,
    val deleteNote: DeleteNote
)