package com.example.android_younotes_app.presentation.notes_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_younotes_app.R
import com.example.android_younotes_app.presentation.ui.theme.BlackGradient
import com.example.android_younotes.presentation.ui.theme.medium

@Preview(showBackground = true)
@Composable
fun NoteItem() {

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .background(BlackGradient)
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Title",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp,
                        color = Color.Gray
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.vector_fill_pin),
                        contentDescription = null,
                        modifier = Modifier.size(8.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}