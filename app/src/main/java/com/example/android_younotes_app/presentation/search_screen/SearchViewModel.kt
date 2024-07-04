package com.example.android_younotes_app.presentation.search_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.SearchRepository
import com.example.android_younotes_app.presentation.add_note.NoteTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor (
) : ViewModel(), SearchRepository {

    private val _searchState = mutableStateOf(
        NoteTextFieldState(
        hint = "Search your note...",
    ))
    val searchState: MutableState<NoteTextFieldState> = _searchState

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    fun initialize(notes: List<Note>) {
        _state.value = state.value.copy(allNotes = notes)
        Log.d("SearchViewModel", "All notes count: ${_state.value.allNotes.size}")
    }

    override fun captureQuery(query: String) {

        if (query.isEmpty()) {
            _state.value = state.value.copy(foundNotes = emptyList())
            Log.d("SearchViewModel", "Query is empty, found notes count = ${_state.value.foundNotes.size}")
        }
        _searchState.value = searchState.value.copy(query = query)
        Log.d("SearchViewModel", "Found notes count: ${state.value.foundNotes.size}")
    }

    fun captureFoundItems(notes: List<Note>) {
        _state.value = state.value.copy(foundNotes = notes)
    }
}