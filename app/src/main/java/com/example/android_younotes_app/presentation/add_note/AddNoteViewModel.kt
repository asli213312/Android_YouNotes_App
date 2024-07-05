package com.example.android_younotes_app.presentation.add_note

import android.content.Context
import android.content.Intent
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
import com.example.android_younotes_app.presentation.add_note.utils.ContextActionAddNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
                            query = note.title,
                            isHintVisible = false
                        )
                        _contentState.value = contentState.value.copy(
                            query = note.content,
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

    fun onContextOption(option: ContextActionAddNote) {
        when(option) {
            is ContextActionAddNote.DeleteInThrash -> {
                if (option.note != null) {

                    viewModelScope.launch {
                        noteUseCases.deleteNoteInThrash(option.note!!.id!!, true)

                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            message = "Note will be in thrash for 7 days"
                        ))
                    }
                }
                else {
                    _currentNoteId?.let { id ->
                        viewModelScope.launch {
                            noteUseCases.deleteNoteInThrash(id, true)

                            _eventFlow.emit(UiEvent.ShowSnackbar(
                                message = "Note will be in thrash for 7 days"
                            ))
                        }
                    }
                }
            }
            is ContextActionAddNote.Share -> {
                fun makeShareContent(content: String) {
                    content.let {
                        viewModelScope.launch {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, content)
                                type = "text/plain"
                            }
                            val chooser = Intent.createChooser(shareIntent, "Share note via")
                            _eventFlow.emit(UiEvent.StartActivity(chooser))
                        }
                    }
                }

                if (option.note == null) {
                    _currentNoteId?.let {
                        viewModelScope.launch {
                            val currentNote = noteUseCases.getNoteById(_currentNoteId!!)
                            makeShareContent(currentNote?.content ?: "")
                        }
                    }
                }
                else {
                    Log.d("AddNoteViewModel", "Selected note to share: ${option.note}")
                    option.note?.let {
                        viewModelScope.launch {
                            makeShareContent(option.note!!.content)
                        }
                    }
                }
            }
            is ContextActionAddNote.SelectColor -> TODO()
            is ContextActionAddNote.Duplicate -> {
                if (option.note == null) {
                    _currentNoteId?.let {
                        viewModelScope.launch {
                            try {
                                noteUseCases.addNote(
                                    Note(
                                        title = _titleState.value.query,
                                        content = _contentState.value.query,
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
                } else {
                    viewModelScope.launch {
                        option.note?.let {
                            try {
                                noteUseCases.addNote(
                                    Note(
                                        title = option.note!!.title,
                                        content = option.note!!.content,
                                        lastChanged = option.note!!.lastChanged,
                                        timeCreated = option.note!!.timeCreated,
                                        isPinned = option.note!!.isPinned,
                                        tag = option.note!!.tag,
                                        backgroundImagePath = option.note!!.backgroundImagePath,
                                        backgroundGradient = option.note!!.backgroundGradient,
                                        previewImagePath = option.note!!.previewImagePath,
                                        id = option.note!!.id!!.plus(1)
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

            else -> {

            }
        }
    }

    fun onEvent(event: AddNoteEvent) {
        when(event) {
            is AddNoteEvent.ChangeContentFocus -> {
                _contentState.value = contentState.value.copy(
                    isHintVisible = !event.focusState.isFocused && contentState.value.query.isBlank()
                )
            }
            is AddNoteEvent.ChangeTitleFocus -> {
                _titleState.value = titleState.value.copy(
                    isHintVisible = !event.focusState.isFocused && titleState.value.query.isBlank()
                )
            }
            is AddNoteEvent.EnteredContent -> {
                _contentState.value = contentState.value.copy(
                    query = event.value
                )
                _additionalState.value = additionalState.value.copy(lastChanged = System.currentTimeMillis())
            }
            is AddNoteEvent.EnteredTitle -> {
                _titleState.value = titleState.value.copy(
                    query = event.value
                )
                _additionalState.value = additionalState.value.copy(lastChanged = System.currentTimeMillis())
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
                                title = _titleState.value.query,
                                content = _contentState.value.query,
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

            is AddNoteEvent.DeleteNote -> {
                event.note.let {
                    viewModelScope.launch {
                        noteUseCases.deleteNote(it)
                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            "Note was deleted forever"
                        ))
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

            else -> {

            }
        }
    }
}

sealed class UiEvent {
    data object OpenGallery : UiEvent()
    data object SaveNote : UiEvent()
    data object BookmarkNote : UiEvent()
    data class StartActivity(val intent: Intent) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}