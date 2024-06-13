package com.example.android_younotes_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_younotes_app.presentation.add_note.AddNoteScreen
import com.example.android_younotes_app.presentation.notes_screen.NotesScreen
import com.example.android_younotes_app.presentation.ui.theme.Android_YouNotesTheme
import com.example.android_younotes_app.presentation.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android_YouNotesTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(Screen.NotesScreen.route) {
                        NotesScreen(
                            this@MainActivity,
                            navController
                        )
                    }
                    composable(Screen.AddNoteScreen.route) {
                        AddNoteScreen(
                            navController = navController,
                            context = applicationContext
                        )
                    }
                }
            }
        }
    }
}