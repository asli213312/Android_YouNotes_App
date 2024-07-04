package com.example.android_younotes_app.presentation.search_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android_younotes_app.presentation._global_components_.NotesTable
import com.example.android_younotes_app.presentation.add_note.AddNoteViewModel
import com.example.android_younotes_app.presentation.add_note.utils.ContextMenuAddNote
import com.example.android_younotes_app.presentation.notes_screen.NotesViewModel
import com.example.android_younotes_app.presentation.ui.theme.Background
import com.example.android_younotes_app.presentation.ui.theme.Primary
import com.example.android_younotes_app.presentation.utils.Screen

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    notesViewModel: NotesViewModel = hiltViewModel(),
    addNoteViewModel: AddNoteViewModel = hiltViewModel(),
    maxLength: Int
) {
    Box(
        modifier = Modifier
            .background(Background)
            .fillMaxSize()
    )
    Box(
        modifier = Modifier
            .background(Primary)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            BasicTextField(
                value = viewModel.searchState.value.query,
                onValueChange = {
                    if (it.length <= maxLength) {
                        viewModel.captureQuery(it)
                        val filteredNotes = notesViewModel.state.value.notes.filter { note ->
                            note.title.startsWith(viewModel.searchState.value.query)
                        }
                        viewModel.captureFoundItems(filteredNotes)
                    }
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp,
                    color = Color.Gray
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (viewModel.searchState.value.query.isEmpty()) {
                        Text(
                            text = viewModel.searchState.value.hint,
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        }
    }
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (viewModel.state.value.foundNotes.isEmpty() || viewModel.searchState.value.query.isEmpty()) {
            Text(
                text = "Your notes will be displayed here",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp,
                    color = Color.Gray
                )
            )
        }
        else {
            Spacer(modifier = Modifier.height(20.dp))

            NotesTable(
                onClickNote = { note ->
                    navController.navigate(
                        Screen.AddNoteScreen.route + "?noteId=${note.id}"
                    )
                },
                //canLoadMedia = notesViewModel.state.value.canLoadMedia,
                canLoadMedia = true,
                notes = viewModel.state.value.foundNotes,
                addNoteViewModel = addNoteViewModel,
                onOption = { option ->
                    when(option) {
                        is ContextMenuAddNote.DeleteInThrash -> {
                            addNoteViewModel.onContextOption(ContextMenuAddNote.DeleteInThrash(option.note))
                        }
                        is ContextMenuAddNote.Duplicate -> {
                            addNoteViewModel.onContextOption(ContextMenuAddNote.Duplicate(option.note))
                        }
                    }
                }
            )
        }
    }
}