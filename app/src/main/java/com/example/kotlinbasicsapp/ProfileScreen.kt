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
import com.google.firebase.database.FirebaseDatabase

@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val userManager = LocalUserManager(context)
    val phone = userManager.getLoggedInPhone() ?: return

    val db = FirebaseDatabase.getInstance().reference

    var user by remember { mutableStateOf<User?>(null) }
    var loading by remember { mutableStateOf(true) }

    // Editable fields
    var name by remember { mutableStateOf("") }
    var extraInfo by remember { mutableStateOf("") }

    // 🔥 Fetch from Firebase
    LaunchedEffect(Unit) {
        db.child("users").child(phone).get()
            .addOnSuccessListener { snapshot ->
                val fetchedUser = snapshot.getValue(User::class.java)
                user = fetchedUser

                fetchedUser?.let {
                    name = it.name
                    extraInfo = if (it.role == "organizer") it.organization else it.interests
                }

                loading = false
            }
    }

    AppScaffold(
        navController = navController,
        title = "Profile",
        showBack = true,
        showBottomBar = false,

        content = { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Role: ${user?.role}")

                TextField(
                    value = extraInfo,
                    onValueChange = { extraInfo = it },
                    label = {
                        if (user?.role == "organizer")
                            Text("Organization")
                        else
                            Text("Interests")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val updatedUser = user?.copy(
                            name = name,
                            interests = if (user?.role == "user") extraInfo else user?.interests ?: "",
                            organization = if (user?.role == "organizer") extraInfo else user?.organization ?: ""
                        )

                        if (updatedUser != null) {
                            db.child("users").child(phone).setValue(updatedUser)
                        }

                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }

                Button(
                    onClick = {
                        userManager.logout()
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
    )}