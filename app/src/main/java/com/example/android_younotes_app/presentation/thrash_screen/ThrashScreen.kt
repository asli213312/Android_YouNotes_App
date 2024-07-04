package com.example.android_younotes_app.presentation.thrash_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android_younotes_app.R
import com.example.android_younotes_app.presentation._global_components_.NotesTable
import com.example.android_younotes_app.presentation._global_components_.SideMenu
import com.example.android_younotes_app.presentation._global_components_.ThemeSearchBar
import com.example.android_younotes_app.presentation.add_note.AddNoteViewModel
import com.example.android_younotes_app.presentation.add_note.UiEvent
import com.example.android_younotes_app.presentation.add_note.utils.ContextMenuDeleteNote
import com.example.android_younotes_app.presentation.notes_screen.NotesViewModel
import com.example.android_younotes_app.presentation.notes_screen.components.GradientFloatingActionButton
import com.example.android_younotes_app.presentation.search_screen.SearchViewModel
import com.example.android_younotes_app.presentation.ui.theme.Background
import com.example.android_younotes_app.presentation.ui.theme.ThemeGradient
import com.example.android_younotes_app.presentation.utils.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ThrashScreen(
    context: Context,
    navController: NavController,
    viewModel: ThrashViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    notesViewModel: NotesViewModel = hiltViewModel(),
    addNoteViewModel: AddNoteViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val contextOptions = listOf(
        ContextMenuDeleteNote.Restore(null)
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG)
                        .show()
                }

                else -> {

                }
            }
        }
    }

    SideMenu(navController = navController, drawerState = drawerState, content = {
        Scaffold(
            floatingActionButton = {
                GradientFloatingActionButton(
                    brush = ThemeGradient,
                    onClick = {
                        viewModel.onEvent(ThrashEvent.DeleteAll)
                    },
                    icon = ImageVector.vectorResource(R.drawable.vector_thrash_fab),
                    iconTint = Color.White,
                    modifier = Modifier.offset(x = (-32).dp, y = (-32).dp)
                )
            }, floatingActionButtonPosition = FabPosition.End, containerColor = Background
        ) { it ->
            ThemeSearchBar(
                drawerState = drawerState,
                placeholderText = searchViewModel.searchState.value.hint,
                navController = navController
            )
            if (notesViewModel.state.value.notes.any { note ->
                    note.isDeleted == true
                })
            {
                viewModel.initialize(notesViewModel.state.value.notes.filter { note ->
                    note.isDeleted == true
                })

                NotesTable(
                    onClickNote = {  note ->
                        navController.navigate(
                            Screen.AddNoteScreen.route + "?noteId=${note.id}"
                        )
                    },
                    canLoadMedia = true,
                    notes = viewModel.state.value.deletedNotes,
                    addNoteViewModel = addNoteViewModel,
                    contextOptions = contextOptions,
                    onOption = { option ->
                        when(option) {
                            is ContextMenuDeleteNote.Restore -> {
                                viewModel.onContextOption(ContextMenuDeleteNote.Restore(option.note))
                            }
                        }
                    }
                )
            }
            else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.vector_thrash_theme),
                        contentDescription = null,
                        modifier = Modifier.size(160.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Your thrash is empty",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.White.copy(0.8f)
                        )
                    )
                }
            }
        }
    })
}