package com.rsupport.saftest.view


import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rsupport.saftest.model.Route


@Composable
fun PermissionRequestScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val intent = Intent(
        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
        Uri.parse("package:" + (context as Activity).packageName)
    )
    val pendingIntent =
        PendingIntent.getActivity(context, 1000, intent, PendingIntent.FLAG_IMMUTABLE)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (Environment.isExternalStorageManager() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            navHostController.navigate(Route.MEDIASTORE_EXPLORER)
        }
    }
    val permissionLauncherImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launcher.launch(IntentSenderRequest.Builder(pendingIntent).build())
        }
    }
    val permissionLauncherVideo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionLauncherImage.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }
    val permissionLauncherAudio = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionLauncherVideo.launch(Manifest.permission.READ_MEDIA_VIDEO)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncherAudio.launch(Manifest.permission.READ_MEDIA_AUDIO)
                } else {
                    launcher.launch(IntentSenderRequest.Builder(pendingIntent).build())
                }

            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "권한 요청")
        }
    }
}