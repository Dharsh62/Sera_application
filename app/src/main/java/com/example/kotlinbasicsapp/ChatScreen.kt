package com.example.kotlinbasicsapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlinbasicsapp.components.AppScaffold
import com.google.firebase.database.*

@Composable
fun ChatScreen(
    navController: NavController,
    chatUser: String
) {

    val database = FirebaseDatabase.getInstance()
    val chatsRef = database.getReference("private_chats")

    // For now we simulate current user
    // Later we will store session properly
    val currentUser = "TEMP_USER"

    // Chat ID sorted alphabetically
    val chatId = if (currentUser < chatUser)
        "chat_${currentUser}_${chatUser}"
    else
        "chat_${chatUser}_${currentUser}"

    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }

    // Realtime Listener
    DisposableEffect(Unit) {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (child in snapshot.children) {
                    val msg = child.getValue(ChatMessage::class.java)
                    msg?.let { messages.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        chatsRef.child(chatId).addValueEventListener(listener)

        onDispose {
            chatsRef.child(chatId).removeEventListener(listener)
        }
    }

    AppScaffold(
        navController = navController,
        title = chatUser,
        showBack = true,
        showBottomBar = false
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
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

                        val msg = ChatMessage(
                            sender = currentUser,
                            text = messageText,
                            timestamp = System.currentTimeMillis()
                        )

                        chatsRef.child(chatId)
                            .push()
                            .setValue(msg)

                        messageText = ""
                    }

                }) {
                    Text("Send")
                }
            }
        }
    }
}