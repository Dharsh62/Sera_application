package com.example.kotlinbasicsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.kotlinbasicsapp.ui.theme.KotlinBasicsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinBasicsAppTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {


                    // ---------------- Splash ----------------
                    composable("splash") {
                        SplashScreen(navController)
                    }

                    // ---------------- Auth Flow ----------------
                    composable("auth_choice") {
                        AuthChoiceScreen(navController)
                    }
                    composable("requests") {
                        RequestsScreen(navController)
                    }
                    composable("phone_auth") {
                        PhoneAuthScreen(navController)
                    }


                    composable(
                        route = "otp/{phone}",
                        arguments = listOf(
                            navArgument("phone") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val phone =
                            backStackEntry.arguments?.getString("phone") ?: ""
                        OtpScreen(navController, phone)
                    }

                    composable(
                        route = "create_profile/{phone}",
                        arguments = listOf(
                            navArgument("phone") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val phone =
                            backStackEntry.arguments?.getString("phone") ?: ""
                        CreateProfileScreen(navController, phone)
                    }

                    // ---------------- Approval ----------------
                    composable("approval") {
                        ApprovalScreen(navController)
                    }

                    // ---------------- Main App ----------------
                    composable("chat_list") {

                        val sampleChats = listOf(
                            ChatItem("1", "Dharsh", "Hey!", System.currentTimeMillis()),
                            ChatItem("2", "Ajay", "Meeting tomorrow", System.currentTimeMillis()),
                            ChatItem("3", "Admin", "Approved", System.currentTimeMillis())
                        )

                        ChatListScreen(
                            chatList = sampleChats,
                            onChatClick = { userId ->
                                navController.navigate("chat/$userId")
                            }
                        )
                    }

                    composable("connect") {
                        ConnectScreen(navController)
                    }
                    composable("profile") {
                        ProfileScreen(navController)
                    }

                    composable(
                        route = "chat/{userId}",
                        arguments = listOf(
                            navArgument("userId") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->

                        val userId =
                            backStackEntry.arguments?.getString("userId") ?: ""

                        ChatScreen(
                            navController = navController,
                            chatUser = userId
                        )
                    }
                }
            }
        }
    }
}