package com.example.kotlinbasicsapp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavController,
    title: String,
    showBack: Boolean = false,
    showBottomBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },

                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                },

                // 🔥 FIX: Combine default + external actions
                actions = {
                    // 👉 Custom actions from screen (like Requests button)
                    actions()

                    // 👉 Default icons
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                    }

                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F2747),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color(0xFF4DA3FF)
                )
            )
        },

        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF0F2747)
                ) {

                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("chat_list") },
                        icon = { Icon(Icons.Default.Chat, null) },
                        label = { Text("Chats") }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Icon(Icons.Default.Group, null) },
                        label = { Text("Groups") }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("connect") },
                        icon = { Icon(Icons.Default.PersonAdd, null) },
                        label = { Text("Connect") }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Icon(Icons.Default.People, null) },
                        label = { Text("Community") }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Icon(Icons.Default.Event, null) },
                        label = { Text("Events") }
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}