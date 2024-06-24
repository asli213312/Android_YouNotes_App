package com.example.android_younotes_app.presentation.notes_screen.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_younotes_app.R
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.models.NoteDefaultGradients
import com.example.android_younotes_app.presentation.ui.theme.BlackGradient
import com.example.android_younotes_app.presentation.ui.theme.ClearGradient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    context: Context,
    hasPermissions: Boolean,
    note: Note,
    onClick: (Note) -> Unit,
) {
    val hasBackground = note.backgroundImagePath != null

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Log.d("NoteItem", "Permission state: $hasPermissions")

    if (hasPermissions) {
        var selectedImageUri: Uri? = null
        if (hasBackground) {
            selectedImageUri = note.backgroundImagePath!!
        } else {
            selectedImageUri = note.previewImagePath
        }

        if (selectedImageUri != null) {
            LaunchedEffect(selectedImageUri) {
                coroutineScope.launch {
                    try {
                        selectedImageUri.let {
                            context.contentResolver.openInputStream(selectedImageUri)
                                ?.use { inputStream ->
                                    bitmap = BitmapFactory.decodeStream(inputStream)
                                }
                        }
                    } catch (e: Exception) {
                        Log.d("NoteItem", "Couldn't load background: ${e.message}")
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick(note) }
    ) {
        Log.d("NoteItem", "background image path: ${note.backgroundImagePath}")
        Log.d("NoteItem", "preview image path: ${note.previewImagePath}")
        Log.d("NoteItem", "bitmap: $bitmap")
        if (bitmap != null && note.backgroundImagePath != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.6f
            )
        }
        Card(
            modifier = Modifier
                .background(
                    brush = if (hasBackground)
                        ClearGradient
                    else if (note.backgroundGradient != null)
                        NoteDefaultGradients.selectGradientByIndex(note.backgroundGradient)
                    else if (note.previewImagePath != null)
                        BlackGradient
                    else BlackGradient,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(0.1f),
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
                .width(120.dp),
                //.padding(horizontal = 16.dp, vertical = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent
            ),
        ) {
            Column {
                if (bitmap != null && note.previewImagePath != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(
                                topStart = 15.dp
                            ))
                            .fillMaxWidth()
                            .height(70.dp),
                        contentScale = ContentScale.FillBounds,
                        alpha = 1f
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Title",
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 22.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White.copy(0.8f)
                                ),
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            AnimatedVisibility(
                                visible = note.isPinned ?: false,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.vector_fill_pin),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                    Text(
                        text = "Example note...",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.White.copy(0.7f),
                        ),
                        maxLines = 6
                    )
                    Spacer(modifier = Modifier.height(26.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jan 3, 2024",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                color = Color.White.copy(0.6f)
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        if (note.tag == null) return@Card

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vector_tag_outline),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Example",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                color = Color.White.copy(0.6f)
                            )
                        )
                    }
                }
            }
        }
    }
}