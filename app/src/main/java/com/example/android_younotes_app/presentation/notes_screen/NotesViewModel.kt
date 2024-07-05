package com.example.android_younotes_app.presentation.notes_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.use_cases.notes.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var _getNotesJob: Job? = null

    init {
        getNotes()
    }

    fun selectNote(note: Note) {
        _state.value = state.value.copy(selectedNote = note)
    }

    private fun getNotes() {
        _getNotesJob?.cancel()
        _getNotesJob = noteUseCases.getNotes()
            .onEach {
                _state.value = this.state.value.copy(
                    notes = it
                )
            }.launchIn(viewModelScope)
    }
}