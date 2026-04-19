package com.example.kotlinbasicsapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.kotlinbasicsapp.components.AppScaffold
import com.google.firebase.database.*

@Composable
fun ConnectScreen(navController: NavController) {

    val context = LocalContext.current
    val userManager = LocalUserManager(context)
    val currentUser = userManager.getLoggedInPhone() ?: return

    val dbRef = FirebaseDatabase.getInstance().getReference("users")
    val db = FirebaseDatabase.getInstance().reference

    var searchQuery by remember { mutableStateOf("") }
    val usersList = remember { mutableStateListOf<User>() }

    DisposableEffect(Unit) {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                usersList.clear()

                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue(User::class.java)

                    if (user != null && user.phone != currentUser) {
                        usersList.add(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        dbRef.addValueEventListener(listener)

        onDispose {
            dbRef.removeEventListener(listener)
        }
    }

    val filteredUsers = usersList.filter { user ->
        user.name.contains(searchQuery, ignoreCase = true)
    }

    AppScaffold(
        navController = navController,
        title = "Connect",

        content = { padding ->

            Column(modifier = Modifier.padding(padding)) {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    placeholder = { Text("Search users...") }
                )

                LazyColumn {
                    items(filteredUsers) { user ->
                        UserItem(user, currentUser, navController)
                    }
                }
            }

        },

        actions = {
            TextButton(
                onClick = {
                    navController.navigate("requests")
                }
            ) {
                Text("Requests")
            }
        }
    )
}

@Composable
fun UserItem(
    user: User,
    currentUser: String,
    navController: NavController
) {

    val db = FirebaseDatabase.getInstance().reference
    var requestSent by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2A41))
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(user.name, color = Color.White)
                Text(user.phone, color = Color.Gray)
            }

            Button(
                onClick = {

                    // 🔥 Check if already connected
                    db.child("connections")
                        .child(currentUser)
                        .child(user.phone)
                        .get()
                        .addOnSuccessListener { snapshot ->

                            if (snapshot.exists()) {
                                // 👉 Go to chat directly
                                navController.navigate("chat/${user.phone}")
                            } else {

                                // 👉 Send request
                                val requestId = db.child("requests").push().key!!

                                val request = mapOf(
                                    "from" to currentUser,
                                    "to" to user.phone,
                                    "status" to "pending"
                                )

                                db.child("requests")
                                    .child(requestId)
                                    .setValue(request)

                                requestSent = true
                            }
                        }
                }
            ) {
                Text(if (requestSent) "Sent" else "Connect")
            }
        }
    }
}