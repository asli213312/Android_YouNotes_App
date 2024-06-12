package com.example.android_younotes_app.presentation.notes_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android_younotes.presentation.ui.theme.medium
import com.example.android_younotes_app.R
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.presentation.notes_screen.components.GradientFloatingActionButton
import com.example.android_younotes_app.presentation.notes_screen.components.NoteItem
import com.example.android_younotes_app.presentation.ui.theme.Background
import com.example.android_younotes_app.presentation.ui.theme.Primary
import com.example.android_younotes_app.presentation.ui.theme.Stroke
import com.example.android_younotes_app.presentation.ui.theme.ThemeGradient
import com.example.android_younotes_app.presentation.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val scaffoldState = rememberBottomSheetScaffoldState()

    val searchText = remember {
        mutableStateOf(TextFieldValue("Search your note..."))
    }

    Scaffold(
        floatingActionButton = {
            GradientFloatingActionButton(
                brush = ThemeGradient,
                onClick = {
                      navController.navigate(Screen.AddNoteScreen.route)
                },
                icon = Icons.Default.Add,
                modifier = Modifier
                    .offset(x = (-32).dp, y = (-32).dp)
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Background
    ) { it ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 30.dp,
                        vertical = 16.dp
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(Primary),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(55.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dehaze,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(
                                horizontal = 8.dp
                            )
                            .background(Primary)
                            .border(
                                border = BorderStroke(1.dp, Stroke),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        BasicTextField(
                            value = searchText.value,
                            onValueChange = {
                                searchText.value = it
                            },
                            textStyle = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                color = Color.Gray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(55.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                SectionTitle(title = "Pinned")
                NotesGrid(notes = state.notes.filter { note -> note.isPinned == true })
                Spacer(modifier = Modifier.height(32.dp))

                SectionTitle(title = "All notes")
                NotesGrid(notes = state.notes.filter { note -> note.isPinned == false })
            }

            if (state.notes.isNotEmpty()) return@Scaffold
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notes_img),
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                )
                Text(
                    text = "There is no notes",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp,
                        color = Color.White
                    )
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pencil_img),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 15.dp),
                        text = "Make a new one",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NotesGrid(notes: List<Note>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(notes) { note ->
            NoteItem(
                onClick = {

                },
                note = note
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontFamily = medium,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            color = Color.White.copy(0.8f)
        ),
        color = Color.White,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 32.dp)
    )
}