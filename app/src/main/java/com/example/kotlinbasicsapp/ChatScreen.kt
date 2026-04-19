package com.example.kotlinbasicsapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.kotlinbasicsapp.components.AppScaffold
import com.google.firebase.database.*
import androidx.compose.ui.Alignment
@Composable
fun ChatScreen(
    navController: NavController,
    chatUser: String
) {

    val context = LocalContext.current
    val userManager = LocalUserManager(context)
    val currentUser = userManager.getLoggedInPhone() ?: return

    val db = FirebaseDatabase.getInstance().reference

    val chatId = if (currentUser < chatUser)
        "${currentUser}_$chatUser"
    else
        "${chatUser}_$currentUser"

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var messageText by remember { mutableStateOf("") }

    var isConnected by remember { mutableStateOf(false) }

    // 🔥 Check connection
    LaunchedEffect(Unit) {
        db.child("connections")
            .child(currentUser)
            .child(chatUser)
            .get()
            .addOnSuccessListener {
                isConnected = it.exists()
            }
    }

    if (!isConnected) {
        AppScaffold(
            navController = navController,
            title = "Chat",
            showBack = true,
            content = { padding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("You are not connected yet")
                }

            }
        )
        return
    }

    DisposableEffect(Unit) {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                messages.clear()

                val temp = mutableListOf<ChatMessage>()

                for (msgSnap in snapshot.children) {
                    val msg = msgSnap.getValue(ChatMessage::class.java)
                    msg?.let { temp.add(it) }
                }

                temp.sortBy { it.timestamp }
                messages.addAll(temp)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        db.child("messages").child(chatId)
            .addValueEventListener(listener)

        onDispose {
            db.child("messages").child(chatId)
                .removeEventListener(listener)
        }
    }

    AppScaffold(
        navController = navController,
        title = chatUser,
        showBack = true,
        showBottomBar = false,

        content = { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(messages) { msg ->

                        val isMe = msg.sender == currentUser

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            if (isMe) Arrangement.End else Arrangement.Start
                        ) {

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor =
                                    if (isMe) Color(0xFF4DA3FF)
                                    else Color(0xFF1B2A41)
                                ),
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    modifier = Modifier.padding(10.dp),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type message") }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {

                        if (messageText.isNotBlank()) {

                            val msgId = db.child("messages")
                                .child(chatId)
                                .push().key!!

                            val msg = ChatMessage(
                                sender = currentUser,
                                text = messageText,
                                timestamp = System.currentTimeMillis()
                            )

                            db.child("messages")
                                .child(chatId)
                                .child(msgId)
                                .setValue(msg)

                            messageText = ""
                        }

                    }) {
                        Text("Send")
                    }
                }
            }

        }
    )
}