package com.example.android_younotes_app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_younotes_app.core.UserPreferencesRepository
import com.example.android_younotes_app.core.UserPreferencesViewModel
import com.example.android_younotes_app.core.UserPreferencesViewModelFactory
import com.example.android_younotes_app.presentation.add_note.AddNoteScreen
import com.example.android_younotes_app.presentation.notes_screen.NotesScreen
import com.example.android_younotes_app.presentation.settings.SettingScreen
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
                val context: Context = LocalContext.current
                val userPreferencesRepository = UserPreferencesRepository(context)
                val userPreferencesViewModel: UserPreferencesViewModel =
                    viewModel(factory = UserPreferencesViewModelFactory(userPreferencesRepository))

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
                    composable(
                        route = Screen.AddNoteScreen.route + "?noteId={noteId}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddNoteScreen(
                            navController = navController,
                            context = context,
                            userPreferencesViewModel = userPreferencesViewModel
                        )
                    }
                    composable(Screen.SettingsScreen.route) {
                        SettingScreen(
                            navController = navController,
                            userViewModel = userPreferencesViewModel
                        )
                    }
                }
            }
        }
    }
}