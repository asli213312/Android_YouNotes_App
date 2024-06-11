package com.example.android_younotes_app.presentation.notes_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_younotes_app.R
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.presentation.ui.theme.BlackGradient

@Composable
fun NoteItem(
    note: Note,
    onClick: (Note) -> Unit
) {

    Card(
        modifier = Modifier
            .background(
                brush = BlackGradient,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .width(100.dp)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        )
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

                if (note.isPinned != null) {
                    IconButton(
                        onClick = { onClick(note) },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vector_fill_pin),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp),
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

            if (note.tag?.isEmpty() == true) return@Card

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