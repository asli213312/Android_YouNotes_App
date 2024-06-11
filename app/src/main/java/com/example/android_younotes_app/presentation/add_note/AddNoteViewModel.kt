package com.example.android_younotes_app.presentation.add_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_younotes_app.domain.models.InvalidNoteException
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.use_cases.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _titleState = mutableStateOf(
        NoteTextFieldState(hint = "Enter title...")
    )

    val titleState: State<NoteTextFieldState> = _titleState

    private val _contentState = mutableStateOf(
        NoteTextFieldState(hint = "Enter some content...")
    )

    val contentState: State<NoteTextFieldState> = _contentState

    private val _lastChanged = mutableLongStateOf(0)

    val lastChanged: Long = _lastChanged.longValue



    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _noteIsBookmarked: Boolean = false
    private var _currentNoteId: Int? = null

    fun onEvent(event: AddNoteEvent) {
        when(event) {
            is AddNoteEvent.ChangeContentFocus -> {
                _contentState.value = contentState.value.copy(
                    isHintVisible = !event.focusState.isFocused && contentState.value.text.isBlank()
                )
            }
            is AddNoteEvent.ChangeTitleFocus -> {
                _titleState.value = titleState.value.copy(
                    isHintVisible = !event.focusState.isFocused && titleState.value.text.isBlank()
                )
            }
            is AddNoteEvent.EnteredContent -> {
                _contentState.value = contentState.value.copy(
                    text = event.value
                )
                _lastChanged.longValue = System.currentTimeMillis()
            }
            is AddNoteEvent.EnteredTitle -> {
                _titleState.value = titleState.value.copy(
                    text = event.value
                )
                _lastChanged.longValue = System.currentTimeMillis()
            }
            is AddNoteEvent.BookmarkNote -> {
                viewModelScope.launch {
                    noteUseCases.bookmarkNote(_currentNoteId)
                }
            }
            AddNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = _titleState.value.text,
                                content = _contentState.value.text,
                                lastChanged = _lastChanged.longValue,
                                timeCreated = System.currentTimeMillis(),
                                isPinned = _noteIsBookmarked.toString(),
                                id = _currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note."
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class UiEvent {
    data object SaveNote : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}