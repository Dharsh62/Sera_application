package com.example.kotlinbasicsapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OtpScreen(navController: NavController, phone: String) {

    val dummyOtp = "1234"
    var enteredOtp by remember { mutableStateOf("") }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {

            Text("OTP sent to $phone")

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = enteredOtp,
                onValueChange = { enteredOtp = it },
                label = { Text("Enter OTP") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (enteredOtp == dummyOtp) {
                        navController.navigate("create_profile/$phone")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify")
            }
        }
    }
}