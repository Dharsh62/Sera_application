package com.example.kotlinbasicsapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.kotlinbasicsapp.components.AppScaffold
import androidx.compose.foundation.layout.PaddingValues

@Composable
fun ChatListScreen(
    chatList: List<ChatItem>,
    onChatClick: (String) -> Unit
) {
    AppScaffold(title = "Chats") { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(chatList) { chat ->
                ChatListItem(chat = chat) {
                    onChatClick(chat.userId)
                }
            }
        }
    }
}
@Composable
fun ChatListItem(
    chat: ChatItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.userName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}