package com.example.kotlinbasicsapp

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current

    val videoUri = Uri.parse(
        "android.resource://${context.packageName}/${R.raw.logo_animation}"
    )

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            volume = 0f
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(Unit) {

        delay(5000)

        val userManager = LocalUserManager(context)
        val loggedInPhone = userManager.getLoggedInPhone()

        Log.d("SPLASH", "Phone: $loggedInPhone")

        if (!loggedInPhone.isNullOrEmpty()) {
            navController.navigate("chat_list") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("phone_auth") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    setBackgroundColor(android.graphics.Color.BLACK)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}