package com.example.android_younotes_app.presentation._global_components_

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.android_younotes.presentation.ui.theme.medium
import com.example.android_younotes_app.presentation._global_components_.utils.DrawerMenuItem
import com.example.android_younotes_app.presentation.ui.theme.Background
import com.example.android_younotes_app.presentation.ui.theme.Primary
import com.example.android_younotes_app.presentation.ui.theme.ThemeGradient
import com.example.android_younotes_app.presentation.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideMenu(
    navController: NavController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    val drawerItems = listOf(
        DrawerMenuItem.Notes,
        DrawerMenuItem.Reminders,
        DrawerMenuItem.Labels,
        DrawerMenuItem.Thrash
    )

    val drawerBottomItems = listOf(
        DrawerMenuItem.AccountSync,
        DrawerMenuItem.Settings
    )
    
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Background,
                drawerContentColor = Color.White
            ) {
                LazyColumn(
                    Modifier
                        .padding(horizontal = 20.dp, vertical = 30.dp)
                ) {
                    item {
                        Text(
                            text = "You Notes",
                            modifier = Modifier
                                .width(100.dp)
                                .padding(start = 10.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 30.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                brush = ThemeGradient
                            )
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                    }

                    itemsIndexed(drawerItems) { index, item ->
                        NavigationDrawerItem(
                            label = {
                                DrawerItem(icon = item.icon, label = item.label)
                            },
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index

                                when (item) {
                                    DrawerMenuItem.AccountSync -> TODO()
                                    DrawerMenuItem.Labels -> TODO()
                                    DrawerMenuItem.Notes -> {
                                        navController.navigate(Screen.NotesScreen.route)
                                    }
                                    DrawerMenuItem.Reminders -> TODO()
                                    DrawerMenuItem.Settings -> {
                                        navController.navigate(Screen.SettingsScreen.route)
                                    }
                                    DrawerMenuItem.Thrash -> {
                                        navController.navigate(Screen.ThrashScreen.route)
                                    }
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Primary,
                                unselectedContainerColor = Color.Transparent,
                                unselectedBadgeColor = Color.Transparent,
                                unselectedIconColor = Color.Transparent,
                                unselectedTextColor = Color.Transparent
                            )
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(280.dp))
                    }

                    itemsIndexed(drawerBottomItems) { index, item ->
                        NavigationDrawerItem(
                            label = { DrawerItem(icon = item.icon, label = item.label) },
                            selected = selectedIndex == index + drawerItems.size,
                            onClick = {
                                selectedIndex = index + drawerItems.size

                                when (item) {
                                    DrawerMenuItem.AccountSync -> TODO()
                                    DrawerMenuItem.Labels -> TODO()
                                    DrawerMenuItem.Notes -> {
                                        navController.navigate(Screen.NotesScreen.route)
                                    }
                                    DrawerMenuItem.Reminders -> TODO()
                                    DrawerMenuItem.Settings -> {
                                        navController.navigate(Screen.SettingsScreen.route)
                                    }
                                    DrawerMenuItem.Thrash -> {
                                        navController.navigate(Screen.ThrashScreen.route)
                                    }
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Primary,
                                unselectedContainerColor = Color.Transparent,
                                unselectedBadgeColor = Color.Transparent,
                                unselectedIconColor = Color.Transparent,
                                unselectedTextColor = Color.Transparent
                            )
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Version 1.0",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                color = Color.Gray.copy(0.5f)
                            ),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }


            }
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        content()
    }
}

@Composable
private fun DrawerItem(icon: Int, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            style = TextStyle(
                fontFamily = medium,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = Color.Gray
            ),
            color = Color.White.copy(0.7f)
        )
    }
}