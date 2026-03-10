package com.example.kotlinbasicsapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlinbasicsapp.components.AppScaffold
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    navController: NavController,
    phone: String
) {

    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("user") }

    var interests by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    AppScaffold(
        navController = navController,
        title = "Create Profile",
        showBack = true,
        showBottomBar = false
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = username,
                onValueChange = { username = it.trim() },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Select Role")

            Row {
                RadioButton(
                    selected = role == "user",
                    onClick = { role = "user" }
                )
                Text("User")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = role == "organizer",
                    onClick = { role = "organizer" }
                )
                Text("Organizer")
            }

            if (role == "user") {
                OutlinedTextField(
                    value = interests,
                    onValueChange = { interests = it },
                    label = { Text("Your Interests") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (role == "organizer") {
                OutlinedTextField(
                    value = organization,
                    onValueChange = { organization = it },
                    label = { Text("Organization Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    if (username.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    loading = true

                    // Check username uniqueness
                    usersRef.child(username).get()
                        .addOnSuccessListener { snapshot ->

                            if (snapshot.exists()) {
                                loading = false
                                Toast.makeText(
                                    context,
                                    "Username already taken",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                val userMap = mapOf(
                                    "phone" to phone,
                                    "password" to password,
                                    "role" to role,
                                    "interests" to interests,
                                    "organization" to organization,
                                    "approved" to if (role == "organizer") false else true
                                )

                                usersRef.child(username)
                                    .setValue(userMap)
                                    .addOnSuccessListener {

                                        loading = false

                                        if (role == "organizer") {
                                            navController.navigate("approval") {
                                                popUpTo("create_profile/$phone") { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate("chat_list") {
                                                popUpTo("create_profile/$phone") { inclusive = true }
                                            }
                                        }
                                    }
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Text("Create Account")
                }
            }
        }
    }
}