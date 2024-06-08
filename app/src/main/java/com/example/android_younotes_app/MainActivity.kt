package com.example.android_younotes_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android_younotes_app.presentation.add_note.AddNoteScreen
import com.example.android_younotes_app.presentation.notes_screen.NotesScreen
import com.example.android_younotes_app.presentation.ui.theme.Android_YouNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android_YouNotesTheme {
                // A surface container using the 'background' color from the theme
                NotesScreen()
            }
        }
    }
}