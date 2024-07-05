package com.example.android_younotes_app.presentation.thrash_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.use_cases.notes.NoteUseCases
import com.example.android_younotes_app.presentation.add_note.UiEvent
import com.example.android_younotes_app.presentation.add_note.utils.ContextActionDeleteNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThrashViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(ThrashState())
    val state: State<ThrashState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun initialize(deletedNotes: List<Note>) {
        _state.value = state.value.copy(deletedNotes = deletedNotes)
    }

    fun onContextOption(option: ContextActionDeleteNote) {
        when (option) {
            is ContextActionDeleteNote.Restore -> {
                Log.d("ThrashViewModel", "Selected note ${option.note}")
                option.note?.let {
                    viewModelScope.launch {
                        noteUseCases.deleteNoteInThrash(option.note!!.id!!, false)
                        _eventFlow.emit(UiEvent.ShowSnackbar("Note was restored!"))
                    }
                }
            }

            else -> {}
        }
    }

    fun onEvent(event: ThrashEvent) {
        when (event) {
            ThrashEvent.DeleteAll -> {
                viewModelScope.launch {
                    _state.value.deletedNotes.forEach { note ->
                        noteUseCases.deleteNote(note)
                    }
                    _eventFlow.emit(UiEvent.ShowSnackbar(
                        message = "All notes were deleted"
                    ))
                }
            }
        }
    }
}