package com.example.android_younotes_app.presentation.add_note

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_younotes_app.domain.models.InvalidNoteException
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.models.NoteDefaultGradients
import com.example.android_younotes_app.domain.use_cases.notes.NoteUseCases
import com.example.android_younotes_app.domain.utils.ImagesUtils
import com.example.android_younotes_app.presentation.add_note.utils.ContextMenuAddNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _titleState = mutableStateOf(
        NoteTextFieldState(hint = "Enter title...")
    )
    val titleState: State<NoteTextFieldState> = _titleState

    private val _contentState = mutableStateOf(
        NoteTextFieldState(hint = "Enter some content...")
    )
    val contentState: State<NoteTextFieldState> = _contentState

    private val _additionalState = mutableStateOf(AddNoteAdditionalState())
    val additionalState: State<AddNoteAdditionalState> = _additionalState

    val selectedBackground = mutableStateOf(false)

    private var _noteIsBookmarked: Boolean? = false
    private var _currentNoteId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteById(noteId)?.also { note ->
                        _currentNoteId = note.id

                        _titleState.value = titleState.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _contentState.value = contentState.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _additionalState.value = additionalState.value.copy(
                            lastChanged = note.lastChanged,
                            timeCreated = note.timeCreated,
                            backgroundImagePath = note.backgroundImagePath?.toString(),
                            backgroundGradient = note.backgroundGradient?.let {
                                NoteDefaultGradients.selectGradientByIndex(note.backgroundGradient)
                            },
                            previewImagePath = note.previewImagePath?.toString(),
                        )
                        _noteIsBookmarked = note.isPinned
                    }
                }
            }
        }
    }

    fun setBackgroundImageUri(uri: Uri, context: Context) {
        viewModelScope.launch {
            _additionalState.value = additionalState.value.copy(
                    backgroundImagePath = uri.toString()
            )
            ImagesUtils.saveImageToFile(uri, context)

            _additionalState.value = additionalState.value.copy(
                previewImagePath = null
            )
        }
    }

    fun setPreviewImageUri(uri: Uri, context: Context) {
        viewModelScope.launch {
            _additionalState.value = additionalState.value.copy(
                previewImagePath = uri.toString()
            )
            ImagesUtils.saveImageToFile(uri, context)

            _additionalState.value = additionalState.value.copy(
                backgroundImagePath = null
            )
        }
    }

    fun onContextOption(option: ContextMenuAddNote) {
        when(option) {
            is ContextMenuAddNote.Delete -> {

            }
            is ContextMenuAddNote.SelectColor -> TODO()
            is ContextMenuAddNote.Duplicate -> {
                _currentNoteId?.let {
                    viewModelScope.launch {
                        try {
                            noteUseCases.addNote(
                                Note(
                                    title = _titleState.value.text,
                                    content = _contentState.value.text,
                                    lastChanged = System.currentTimeMillis(),
                                    timeCreated = System.currentTimeMillis(),
                                    isPinned = _noteIsBookmarked,
                                    tag = _additionalState.value.noteTag,
                                    backgroundImagePath = _additionalState.value.backgroundImagePath?.let {
                                        Uri.parse(_additionalState.value.backgroundImagePath)
                                    },
                                    backgroundGradient = additionalState.value.backgroundGradient?.index,
                                    previewImagePath = _additionalState.value.previewImagePath?.let {
                                        Uri.parse(_additionalState.value.previewImagePath)
                                    },
                                    id = _currentNoteId!!.plus(1)
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
                _additionalState.value.copy(lastChanged = System.currentTimeMillis())
            }
            is AddNoteEvent.EnteredTitle -> {
                _titleState.value = titleState.value.copy(
                    text = event.value
                )
                _additionalState.value.copy(lastChanged = System.currentTimeMillis())
            }
            is AddNoteEvent.BookmarkNote -> {
                viewModelScope.launch {
                    if (_noteIsBookmarked == false) {
                        _currentNoteId?.let {
                            noteUseCases.bookmarkNote(_currentNoteId!!, true)
                        }

                        if (_currentNoteId == null)
                            _noteIsBookmarked = true;

                        _eventFlow.emit(UiEvent.BookmarkNote)
                    }
                    else {
                        _currentNoteId?.let {
                            noteUseCases.bookmarkNote(_currentNoteId!!, false)
                        }

                        if (_currentNoteId == null)
                            _noteIsBookmarked = false
                    }
                }
            }
            is AddNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = _titleState.value.text,
                                content = _contentState.value.text,
                                lastChanged = System.currentTimeMillis(),
                                timeCreated = System.currentTimeMillis(),
                                isPinned = _noteIsBookmarked,
                                tag = _additionalState.value.noteTag,
                                backgroundImagePath = _additionalState.value.backgroundImagePath?.let {
                                    Uri.parse(_additionalState.value.backgroundImagePath)
                                },
                                backgroundGradient = additionalState.value.backgroundGradient?.index,
                                previewImagePath = _additionalState.value.previewImagePath?.let {
                                    Uri.parse(_additionalState.value.previewImagePath)
                                },
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

            is AddNoteEvent.AddBackground -> {
                viewModelScope.launch {
                    selectedBackground.value = true
                    _eventFlow.emit(UiEvent.OpenGallery)
                }
            }
            is AddNoteEvent.AddPreview -> {
                viewModelScope.launch {
                    selectedBackground.value = false
                    _eventFlow.emit(UiEvent.OpenGallery)
                }
            }
        }
    }
}

sealed class UiEvent {
    data object OpenGallery : UiEvent()
    data object SaveNote : UiEvent()
    data object BookmarkNote : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}