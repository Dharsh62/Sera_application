package com.example.kotlinbasicsapp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.kotlinbasicsapp.components.AppScaffold
import com.google.firebase.database.*
@Composable
fun RequestsScreen(navController: NavController) {

    val context = LocalContext.current
    val userManager = LocalUserManager(context)
    val currentUser = userManager.getLoggedInPhone() ?: return

    val db = FirebaseDatabase.getInstance().reference

    val requests = remember { mutableStateListOf<Pair<String, Request>>() }

    DisposableEffect(Unit) {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                requests.clear()

                for (reqSnap in snapshot.children) {
                    val request = reqSnap.getValue(Request::class.java)

                    if (request != null &&
                        request.to == currentUser &&
                        request.status == "pending"
                    ) {
                        requests.add(reqSnap.key!! to request)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        db.child("requests").addValueEventListener(listener)

        onDispose {
            db.child("requests").removeEventListener(listener)
        }
    }

    AppScaffold(
        navController = navController,
        title = "Requests",

        content = { padding ->

            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                items(requests) { (requestId, request) ->
                    RequestItem(requestId, request, currentUser)
                }
            }

        }
    )
}
@Composable
fun RequestItem(
    requestId: String,
    request: Request,
    currentUser: String
) {

    val db = FirebaseDatabase.getInstance().reference

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("From: ${request.from}")

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(onClick = {

                    // ✅ Accept request
                    db.child("requests")
                        .child(requestId)
                        .child("status")
                        .setValue("accepted")

                    // 🔥 Create connection
                    db.child("connections")
                        .child(currentUser)
                        .child(request.from)
                        .setValue(true)

                    db.child("connections")
                        .child(request.from)
                        .child(currentUser)
                        .setValue(true)

                }) {
                    Text("Accept")
                }

                Button(
                    onClick = {
                        db.child("requests")
                            .child(requestId)
                            .child("status")
                            .setValue("rejected")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Reject")
                }
            }
        }
    }
}