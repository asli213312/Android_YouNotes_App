package com.example.android_younotes_app.presentation._global_components_

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.presentation.add_note.AddNoteViewModel
import com.example.android_younotes_app.presentation.notes_screen.NotesGrid
import com.example.android_younotes_app.presentation.notes_screen.SectionTitle
import com.example.android_younotes_app.presentation.utils.Screen

@Composable
fun NotesTable(
    onClickNote: (Note) -> Unit,
    canLoadMedia: Boolean,
    notes: List<Note>,
    addNoteViewModel: AddNoteViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        if (canLoadMedia && notes.isNotEmpty()) {
            SectionTitle(title = "Pinned")
            NotesGrid(
                notes = notes.filter { note -> note.isPinned == true },
                canLoadMedia = canLoadMedia,
                onClick = { note -> onClickNote(note) },
                addNoteViewModel = addNoteViewModel
            )
            Spacer(modifier = Modifier.height(32.dp))

            SectionTitle(title = "All notes")
            NotesGrid(
                notes = notes.filter { note -> note.isPinned == false },
                canLoadMedia = canLoadMedia,
                onClick = { note -> onClickNote(note) },
                addNoteViewModel = addNoteViewModel
            )
        }
    }
}