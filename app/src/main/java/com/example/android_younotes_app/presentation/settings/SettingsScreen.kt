package com.example.android_younotes_app.presentation.settings

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.android_younotes_app.core.UserPreferencesViewModel
import com.example.android_younotes_app.presentation._global_components_.SideMenu
import com.example.android_younotes_app.presentation.settings.components.GradientSwitch
import com.example.android_younotes_app.presentation.ui.theme.Background
import com.example.android_younotes_app.presentation.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    navController: NavController,
    userViewModel: UserPreferencesViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val userState by userViewModel.state

    SideMenu(
        navController = navController,
        drawerState = drawerState,
        content = {
            Scaffold(
                containerColor = Background
            ) { it ->
                Box(
                    modifier = Modifier
                    //.fillMaxSize()
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Primary)
                                .padding(start = 8.dp)
                                .padding(bottom = 15.dp, top = 25.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(32.dp))
                                Text(
                                    text = "Settings",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        letterSpacing = 0.5.sp,
                                        color = Color.White.copy(0.4f)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(36.dp))
                        }

                        Column(
                            modifier = Modifier
                                .padding(50.dp)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Theme",
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White.copy(0.4f)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            SwitchItem(
                                label = "Using system theme",
                                onClick = { state ->
                                    userViewModel.saveThemeSetting(state)
                                },
                                initialValue = userState.isSystemTheme
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun SwitchItem(
    label: String,
    onClick: (Boolean) -> Unit,
    initialValue: Boolean
) {

    var isChecked by remember { mutableStateOf(initialValue) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = Color.White.copy(0.8f)
            )
        )
        GradientSwitch(
            modifier = Modifier,
            isChecked = isChecked,
            onCheckedChange = {
                isChecked = it
                onClick(it)
            },
            size = 40.dp
        )
    }
}