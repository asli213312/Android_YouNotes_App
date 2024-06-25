package com.example.android_younotes_app.domain.models

import android.net.Uri
import androidx.compose.ui.graphics.Brush
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android_younotes_app.presentation.ui.theme.BlackGradient
import com.example.android_younotes_app.presentation.ui.theme.GreenGradient
import com.example.android_younotes_app.presentation.ui.theme.RedGradient
import com.example.android_younotes_app.presentation.ui.theme.ThemeGradient

@Entity
data class Note(
    val title: String,
    val content: String,
    val lastChanged: Long,
    val timeCreated: Long,
    val isPinned: Boolean? = null,
    val tag: String? = null,
    val backgroundImagePath: Uri? = null,
    val backgroundGradient: Int? = null,
    val previewImagePath: Uri? = null,
    @PrimaryKey
    val id: Int? = null
)

sealed class NoteDefaultGradients {
    companion object {
        val RED: NoteGradient = NoteGradient.RED
        val GREEN: NoteGradient = NoteGradient.GREEN
        val THEME: NoteGradient = NoteGradient.THEME

        fun selectGradientByIndex(index: Int): NoteGradient {
            when(index) {
                0 -> return NoteGradient.RED
                1 -> return NoteGradient.GREEN
                2 -> return NoteGradient.THEME
            }

            return NoteGradient.THEME
        }
    }
}

sealed class NoteGradient(
    val brush: Brush,
    val index: Int
) {
    data object RED: NoteGradient(RedGradient, 0)
    data object GREEN: NoteGradient(GreenGradient, 1)
    data object THEME: NoteGradient(ThemeGradient, 2)
}

enum class NoteCategory {
    DEFAULT,
    BOOKMARKED,
    ARCHIVED
}

class InvalidNoteException(message: String) : Exception(message)