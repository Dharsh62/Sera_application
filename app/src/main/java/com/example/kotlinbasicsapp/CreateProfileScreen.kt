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
    val userManager = LocalUserManager(context)

    val db = FirebaseDatabase.getInstance().reference

    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("user") }
    var interests by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    AppScaffold(
        navController = navController,
        title = "Create Profile",
        showBack = true,
        showBottomBar = false,

        content = { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
            ) {
                // your profile creation UI
            }

        }
    ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
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

                    if (name.isBlank()) {
                        Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    loading = true

                    val user = User(
                        name = name,
                        phone = phone,
                        interests = interests,
                        organization = organization,
                        role = role,
                        approved = role == "user"
                    )

                    // 🔥 STORE USING PHONE AS KEY
                    db.child("users").child(phone)
                        .setValue(user)
                        .addOnSuccessListener {

                            loading = false
                            userManager.setLoggedIn(phone)

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
