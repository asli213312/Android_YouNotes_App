package com.example.android_younotes_app.presentation.add_note

import androidx.compose.ui.focus.FocusState
import com.example.android_younotes_app.domain.models.Note

sealed class AddNoteEvent{
    data class EnteredTitle(val value: String): AddNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddNoteEvent()
    data class EnteredContent(val value: String): AddNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddNoteEvent()
    data object SaveNote: AddNoteEvent()
    data object AddBackground: AddNoteEvent()
    data object AddPreview: AddNoteEvent()
    data object BookmarkNote: AddNoteEvent()
}