package com.example.kotlinbasicsapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.kotlinbasicsapp.components.AppScaffold
import androidx.compose.foundation.layout.PaddingValues

@Composable
fun ApprovalScreen(navController: NavController) {

    AppScaffold(
        navController = navController,
        title = "Approval Pending"
    ) { padding: PaddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A192F))
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your organization is under review.\nPlease wait for approval."
            )
        }
    }
}