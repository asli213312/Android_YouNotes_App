package com.example.android_younotes_app.presentation.notes_screen

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android_younotes.presentation.ui.theme.medium
import com.example.android_younotes_app.R
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.repository.SelectableNoteRepository
import com.example.android_younotes_app.domain.utils.CheckPermissions
import com.example.android_younotes_app.presentation._global_components_.GradientButton
import com.example.android_younotes_app.presentation._global_components_.NotesTable
import com.example.android_younotes_app.presentation._global_components_.SideMenu
import com.example.android_younotes_app.presentation._global_components_.ThemeSearchBar
import com.example.android_younotes_app.presentation.add_note.AddNoteEvent
import com.example.android_younotes_app.presentation.search_screen.SearchViewModel
import com.example.android_younotes_app.presentation.add_note.AddNoteViewModel
import com.example.android_younotes_app.presentation.add_note.UiEvent
import com.example.android_younotes_app.presentation.add_note.utils.ContextActionAbstract
import com.example.android_younotes_app.presentation.add_note.utils.ContextActionAddNote
import com.example.android_younotes_app.presentation.notes_screen.components.GradientFloatingActionButton
import com.example.android_younotes_app.presentation.notes_screen.components.NoteItem
import com.example.android_younotes_app.presentation.thrash_screen.ThrashViewModel
import com.example.android_younotes_app.presentation.ui.theme.Background
import com.example.android_younotes_app.presentation.ui.theme.Primary
import com.example.android_younotes_app.presentation.ui.theme.Stroke
import com.example.android_younotes_app.presentation.ui.theme.ThemeGradient
import com.example.android_younotes_app.presentation.utils.Screen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    context: Context,
    activity: Activity,
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    addNoteViewModel: AddNoteViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val deleteAlertState = remember() {
        mutableStateOf(false)
    }

    val canLoadMedia = remember {
        mutableStateOf(state.canLoadMedia)
    }

    val contextOptions = listOf(
        ContextActionAddNote.DeleteInThrash(null),
        ContextActionAddNote.Duplicate(null),
        ContextActionAddNote.Share(null)
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        canLoadMedia.value = isGranted
    }

    if (CheckPermissions.hasPermission(
            100,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            LocalContext.current,
            activity = activity
        )
    ) {
        canLoadMedia.value = true
        Log.d("NotesScreen", "Permission access: ${canLoadMedia.value}")
    } else {
        SideEffect {
            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionLauncher.launch(android.Manifest.permission.MANAGE_DOCUMENTS)
        }
    }

    LaunchedEffect(key1 = true) {
        addNoteViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG)
                        .show()
                }
                is UiEvent.StartActivity -> {
                    Log.d("NotesScreen", "Should be launch intent: ${event.intent}")
                    event.intent.let {
                        Log.d("NotesScreen", "Launched intent: ${event.intent}")
                        context.startActivity(it)
                    }
                }

                else -> {

                }
            }
        }
    }

    searchViewModel.initialize(state.notes)

    SideMenu(navController = navController, drawerState = drawerState, content = {
        Scaffold(
            floatingActionButton = {
                GradientFloatingActionButton(
                    brush = ThemeGradient,
                    onClick = {
                        navController.navigate(Screen.AddNoteScreen.route)
                    },
                    icon = ImageVector.vectorResource(R.drawable.vector_add_note),
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

            if (state.notes.isNotEmpty() and state.notes.any { note ->
                    note.isDeleted == null || note.isDeleted == false
                }) {
                Column(Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(70.dp))
                    NotesTable(
                        onClickNote = { note ->
                            navController.navigate(
                                Screen.AddNoteScreen.route + "?noteId=${note.id}"
                            )
                        },
                        canLoadMedia = canLoadMedia.value,
                        notes = state.notes.filter { note ->
                            note.isDeleted == null || note.isDeleted == false
                        },
                        addNoteViewModel = addNoteViewModel,
                        contextOptions = contextOptions,
                        onOption = { option ->
                            when (option) {
                                is ContextActionAddNote.DeleteInThrash -> {
                                    option.note?.let {
                                        deleteAlertState.value = true
                                        viewModel.selectNote(option.note!!)
                                    }
                                    //addNoteViewModel.onContextOption(
                                    //    ContextActionAddNote.DeleteInThrash(
                                    //        option.note
                                    //    )
                                    //)
                                }

                                is ContextActionAddNote.Duplicate -> {
                                    addNoteViewModel.onContextOption(
                                        ContextActionAddNote.Duplicate(
                                            option.note
                                        )
                                    )
                                }

                                is ContextActionAddNote.Share -> {
                                    addNoteViewModel.onContextOption(
                                        ContextActionAddNote.Share(
                                            option.note
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }
            else {
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
                        text = "There is no notes", style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.White
                        )
                    )
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
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

            if (!deleteAlertState.value) return@Scaffold
            AlertDialog(
                onDismissRequest = { deleteAlertState.value = false },
                modifier = Modifier
                    .height(300.dp)
                    .width(600.dp)
                    .padding(horizontal = 40.dp, vertical = 26.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Primary.copy(1f),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .border(2.dp, Stroke, RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Delete this note?",
                            style = TextStyle(
                                fontFamily = medium,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = "This action will move your note to thrash. " +
                                    "Check below to delete it permanently.",
                            style = TextStyle(
                                fontFamily = medium,
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp,
                                color = Color.White.copy(0.7f)
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )

                        val isChecked = remember {
                            mutableStateOf(false)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = isChecked.value,
                                onCheckedChange = { isChecked.value = it }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Delete permanently",
                                style = TextStyle(
                                    fontFamily = medium,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(15.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    if (isChecked.value) {
                                        addNoteViewModel.onEvent(
                                            AddNoteEvent.DeleteNote(viewModel.state.value.selectedNote!!)
                                        )
                                    }
                                    else {
                                        addNoteViewModel.onContextOption(
                                            ContextActionAddNote.DeleteInThrash(
                                                viewModel.state.value.selectedNote
                                            ))
                                    }
                                    deleteAlertState.value = false
                                 },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .border(1.dp, Stroke, RoundedCornerShape(8.dp))
                                    .width(90.dp)
                                    .height(30.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Background
                                )
                            ) {
                                Text(
                                    text = "Delete",
                                    style = TextStyle(
                                        fontFamily = medium,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp,
                                        lineHeight = 24.sp,
                                        letterSpacing = 0.5.sp,
                                        color = Color.White
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            GradientButton(
                                brush = ThemeGradient,
                                shape = RoundedCornerShape(8.dp),
                                onClick = { deleteAlertState.value = false },
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(30.dp)
                            ) {
                                Text(
                                    text = "Cancel",
                                    style = TextStyle(
                                        fontFamily = medium,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
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
        }
    })
}

@Composable
fun NotesGrid(
    notes: List<Note>,
    canLoadMedia: Boolean,
    options: List<ContextActionAbstract>,
    onOption: (ContextActionAbstract) -> Unit,
    onClick: (Note) -> Unit,
    addNoteViewModel: AddNoteViewModel
) {

    var contextMenuState by remember { mutableStateOf(false) }
    var selectedNotePos by remember { mutableStateOf(DpOffset.Zero) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(notes) { note ->
            NoteItem(note = note,
                hasPermissions = canLoadMedia,
                context = LocalContext.current,
                onClick = { onClick(note) },
                onLongPress = { coordinates ->
                    contextMenuState = true
                    selectedNote = note

                    Log.d("NotesScreen", "ContextMenuState: $contextMenuState")
                },
                setCoordinates = { coords ->
                    selectedNotePos = coords
                })
        }
    }

    LongTapContextMenu(
        state = contextMenuState,
        selectedNotePos = selectedNotePos,
        onOptionSelected = { option ->
            onOption(option)
            contextMenuState = false
        },
        onDismiss = { contextMenuState = false },
        options = options,
        selectedNote = selectedNote
    )
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title, style = TextStyle(
            fontFamily = medium,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            color = Color.White.copy(0.8f)
        ), color = Color.White, modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 32.dp)
    )
}

@Composable
private fun LongTapContextMenu(
    state: Boolean,
    selectedNotePos: DpOffset,
    options: List<ContextActionAbstract>,
    selectedNote: Note?,
    onOptionSelected: (ContextActionAbstract) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedNotePosState by remember { mutableStateOf(selectedNotePos) }

    AnimatedVisibility(
        visible = state,
        enter = fadeIn(animationSpec = tween(1000)) + expandVertically(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000)) + shrinkVertically(animationSpec = tween(1000))
    ) {
        DropdownMenu(
            expanded = state, onDismissRequest = {
                onDismiss.invoke()
            }, offset = DpOffset(
                x = selectedNotePosState.x + 30.dp, y = selectedNotePosState.y
            ), modifier = Modifier.background(Background)
        ) {
            options.forEachIndexed { index, selectedOption ->
                DropdownMenuItem(onClick = {
                    if (selectedOption is SelectableNoteRepository) {
                        val selectableOption: SelectableNoteRepository = selectedOption
                        selectableOption.invoke(selectedNote!!)
                    }
                    onOptionSelected(selectedOption)
                }, text = {
                    val option = options[index]

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(option.icon),
                            contentDescription = null,
                            tint = option.color,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = options[index].title, color = Color.White
                        )
                    }
                })
            }
        }
    }
}