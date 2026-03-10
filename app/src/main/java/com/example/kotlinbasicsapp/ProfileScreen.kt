package com.example.kotlinbasicsapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlinbasicsapp.components.AppScaffold

@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val loggedPhone = LocalUserManager.getLoggedInPhone(context)

    val prefs = context.getSharedPreferences("app_users", android.content.Context.MODE_PRIVATE)

    var username by remember {
        mutableStateOf(
            prefs.getString("username_$loggedPhone", "") ?: ""
        )
    }

    var role by remember {
        mutableStateOf(
            prefs.getString("role_$loggedPhone", "User") ?: "User"
        )
    }

    var extraInfo by remember {
        mutableStateOf(
            prefs.getString("extra_$loggedPhone", "") ?: ""
        )
    }

    AppScaffold(
        navController = navController,
        title = "Profile",
        showBack = true,
        showBottomBar = false
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Role: $role")

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = extraInfo,
                onValueChange = { extraInfo = it },
                label = {
                    if (role == "Organizer")
                        Text("Organization Details")
                    else
                        Text("Interests")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    prefs.edit()
                        .putString("username_$loggedPhone", username)
                        .putString("extra_$loggedPhone", extraInfo)
                        .apply()

                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    LocalUserManager.logout(context)

                    navController.navigate("auth_choice") {
                        popUpTo("chat_list") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }
    }
}