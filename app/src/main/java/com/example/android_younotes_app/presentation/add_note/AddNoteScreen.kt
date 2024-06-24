package com.example.android_younotes_app.presentation.add_note

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.example.android_younotes_app.R
import com.example.android_younotes_app.presentation.ui.theme.Stroke
import com.example.android_younotes.presentation.ui.theme.light
import com.example.android_younotes.presentation.ui.theme.medium
import com.example.android_younotes_app.core.UserPreferencesViewModel
import com.example.android_younotes_app.domain.models.NoteDefaultGradients
import com.example.android_younotes_app.domain.utils.ImagesUtils
import com.example.android_younotes_app.presentation._global_components_.GradientButton
import com.example.android_younotes_app.presentation.add_note.utils.ContextMenuAddNote
import com.example.android_younotes_app.presentation.ui.theme.Background
import com.example.android_younotes_app.presentation.ui.theme.BlackGradient
import com.example.android_younotes_app.presentation.ui.theme.Primary
import com.example.android_younotes_app.presentation.ui.theme.ThemeGradient
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    context: Context,
    viewModel: AddNoteViewModel = hiltViewModel(),
    userPreferencesViewModel: UserPreferencesViewModel
) {
    val title by viewModel.titleState
    val content by viewModel.contentState

    val additionalState by viewModel.additionalState
    val userState by userPreferencesViewModel.state

    val backgroundImg: Bitmap? by produceState<Bitmap?>(
        initialValue = null,
        additionalState.backgroundImagePath
    ) {
        value = additionalState.backgroundImagePath?.let {
            val uri = Uri.parse(it)
            ImagesUtils.loadImageFromUri(uri, context)
        }
    }

    val previewImg: Bitmap? by produceState<Bitmap?>(
        initialValue = null,
        additionalState.previewImagePath
    ) {
        value = additionalState.previewImagePath?.let {
            val uri = Uri.parse(it)
            ImagesUtils.loadImageFromUri(uri, context)
        }
    }

    val spacerPreview by animateDpAsState(
        targetValue =
        if (previewImg == null)
            80.dp
        else 70.dp,
        label = ""
    )

    val isContextMenuExpanded = remember() {
        mutableStateOf(false)
    }

    val isNotSavedAlertExpanded = remember {
        mutableStateOf(false)
    }

    val defaultNoteGradients = listOf(
        NoteDefaultGradients.RED,
        NoteDefaultGradients.GREEN,
        NoteDefaultGradients.THEME
    )

    val menuOptions = listOf(
        ContextMenuAddNote.Delete,
        ContextMenuAddNote.SelectColor
    )

    var selectColorDialogIsOpen by remember {
        mutableStateOf(false)
    }

    val launcherForContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {

            if (!viewModel.selectedBackground.value)
                viewModel.setPreviewImageUri(it, context)
            else
                viewModel.setBackgroundImageUri(it, context)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.SaveNote -> {
                    Toast.makeText(context, "Note saved!", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }

                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, "Something got wrong... try later", Toast.LENGTH_LONG)
                        .show()
                }

                is UiEvent.BookmarkNote -> {
                    Toast.makeText(context, "Note bookmarked!", Toast.LENGTH_SHORT).show()
                }

                UiEvent.OpenGallery -> {
                    launcherForContent.launch("image/*")
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (backgroundImg != null && previewImg == null) {
            Image(
                bitmap = backgroundImg!!.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.3f
            )
        } else if (additionalState.backgroundGradient == null && userState.isSystemTheme) {
            Image(
                painter = painterResource(id = R.drawable.system_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.3f
            )
        } else if (additionalState.backgroundGradient != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = additionalState.backgroundGradient!!.brush
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = BlackGradient
                    )
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .padding(horizontal = 22.dp),
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {

                            if (viewModel.contentState.value.text.isEmpty()
                                && viewModel.titleState.value.text.isEmpty()
                            )
                                navController.navigateUp()
                            else if (viewModel.contentState.value.text.isNotEmpty()
                                && viewModel.titleState.value.text.isNotEmpty()) {
                                isNotSavedAlertExpanded.value = true
                            }
                            else {
                                navController.navigateUp()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            viewModel.onEvent(AddNoteEvent.BookmarkNote)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vector_pin),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    Row(
                        Modifier
                            .border(
                                border = BorderStroke(1.dp, Stroke),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .padding(end = 8.dp)
                            .clickable {
                                viewModel.onEvent(AddNoteEvent.SaveNote)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.vector_save),
                                contentDescription = null,
                                modifier = Modifier.size(19.dp),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Save",
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
        Column(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .padding(horizontal = 22.dp)
                .padding(start = 12.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(spacerPreview))

            if (previewImg != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = previewImg!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .matchParentSize(),
                        alpha = 1f
                    )
                    BasicTextField(
                        value = title.text,
                        onValueChange = {
                            viewModel.onEvent(AddNoteEvent.EnteredTitle(it))
                        },
                        textStyle = TextStyle(
                            fontFamily = medium,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (previewImg == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .padding(bottom = 4.dp)
                ) {
                    BasicTextField(
                        value = title.text,
                        onValueChange = {
                            viewModel.onEvent(AddNoteEvent.EnteredTitle(it))
                        },
                        textStyle = TextStyle(
                            fontFamily = medium,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(
                    if (previewImg == null)
                        10.dp
                    else 20.dp
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .padding(bottom = 30.dp)
            ) {
                BasicTextField(
                    value = content.text,
                    onValueChange = {
                        viewModel.onEvent(AddNoteEvent.EnteredContent(it))
                    },
                    textStyle = TextStyle(
                        fontFamily = light,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Last changed : Feb 18, 2023 at 2:00AM",
                style = TextStyle(
                    fontFamily = light,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp,
                    color = Color.White.copy(0.5f)
                ),
                modifier = Modifier.align(Alignment.End)
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.onEvent(AddNoteEvent.AddBackground) }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vector_gallery),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp)
                                .weight(0.1f),
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = { viewModel.onEvent(AddNoteEvent.AddPreview) }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vector_camera),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp),
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vector_mic),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Created: Mar 1, 2024",
                        style = TextStyle(
                            fontFamily = light,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.White.copy(0.8f)
                        ),
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    IconButton(
                        onClick = { isContextMenuExpanded.value = true }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vector_vertical_dots),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp),
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = isContextMenuExpanded.value,
                        onDismissRequest = { isContextMenuExpanded.value = false },
                        offset = DpOffset(230.dp, (-50).dp),
                        modifier = Modifier
                            .background(Background)
                    ) {
                        menuOptions.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                onClick = {
                                    when (index) {
                                        0 -> viewModel.onContextOption(ContextMenuAddNote.Delete)
                                        1 -> selectColorDialogIsOpen = true
                                    }
                                    isContextMenuExpanded.value = false
                                },
                                text = {
                                    val option = menuOptions[index]

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
                                            text = menuOptions[index].title,
                                            color = Color.White
                                        )
                                    }
                                }
                            )
                        }
                    }

                    if (isNotSavedAlertExpanded.value) {
                        AlertDialog(
                            onDismissRequest = { isNotSavedAlertExpanded.value = false },
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
                                        text = "Save this note?",
                                        style = TextStyle(
                                            fontFamily = medium,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 24.sp,
                                            lineHeight = 24.sp,
                                            letterSpacing = 0.5.sp,
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                    Spacer(modifier = Modifier.height(30.dp))

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
                                            text = "Enable auto save",
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

                                    Spacer(modifier = Modifier.width(15.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        GradientButton(
                                            brush = ThemeGradient,
                                            shape = RoundedCornerShape(8.dp),
                                            onClick = { viewModel.onEvent(AddNoteEvent.SaveNote) },
                                            modifier = Modifier
                                                .width(60.dp)
                                                .height(30.dp)
                                        ) {
                                            Text(
                                                text = "Save",
                                                style = TextStyle(
                                                    fontFamily = medium,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp,
                                                    lineHeight = 24.sp,
                                                    letterSpacing = 0.5.sp,
                                                    color = Color.White
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))

                                        Button(
                                            onClick = { navController.navigateUp() },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .border(1.dp, Stroke, RoundedCornerShape(8.dp))
                                                .width(120.dp)
                                                .height(30.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Background
                                            )
                                        ) {
                                            Text(
                                                text = "Discard",
                                                style = TextStyle(
                                                    fontFamily = medium,
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

                        if (!selectColorDialogIsOpen) return@Column
                        AlertDialog(
                            onDismissRequest = { selectColorDialogIsOpen = false },
                            modifier = Modifier.height(200.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Primary.copy(1f),
                                        shape = RoundedCornerShape(32.dp)
                                    )
                                    .border(1.dp, Stroke, RoundedCornerShape(32.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Select color",
                                        style = TextStyle(
                                            fontFamily = medium,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 18.sp,
                                            lineHeight = 24.sp,
                                            letterSpacing = 0.5.sp,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        defaultNoteGradients.forEach { gradient ->
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        brush = gradient.brush,
                                                        shape = CircleShape
                                                    )
                                                    .size(60.dp)
                                                    .clickable {
                                                        additionalState.backgroundGradient =
                                                            gradient
                                                        selectColorDialogIsOpen = false
                                                    }
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}