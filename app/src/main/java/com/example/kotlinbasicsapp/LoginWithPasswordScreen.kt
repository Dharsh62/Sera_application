package com.example.kotlinbasicsapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginWithPasswordScreen(navController: NavController) {

    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {

            Text("Login", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    val prefs = context.getSharedPreferences("app_users", android.content.Context.MODE_PRIVATE)

                    val allEntries = prefs.all

                    var matchedPhone: String? = null

                    allEntries.forEach { entry ->
                        if (entry.key.startsWith("username_") && entry.value == username) {
                            matchedPhone = entry.key.removePrefix("username_")
                        }
                    }

                    if (matchedPhone != null) {

                        val savedPassword = prefs.getString("password_$matchedPhone", "")

                        if (savedPassword == password) {

                            LocalUserManager.setLoggedIn(context, matchedPhone!!)

                            navController.navigate("chat_list") {
                                popUpTo("login_password") { inclusive = true }
                            }

                        } else {
                            Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(context, "Username not found", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}