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

                    composable("phone_auth") {
                        PhoneAuthScreen(navController)
                    }

                    composable("login_password") {
                        LoginWithPasswordScreen(navController)
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
                        ChatListScreen(navController)
                    }

                    composable("connect") {
                        ConnectScreen(navController)
                    }
                    composable("profile") {
                        ProfileScreen(navController)
                    }

                    composable(
                        route = "chat_screen/{username}",
                        arguments = listOf(
                            navArgument("username") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->

                        val username =
                            backStackEntry.arguments?.getString("username") ?: ""

                        ChatScreen(
                            navController = navController,
                            chatUser = username
                        )
                    }
                }
            }
        }
    }
}