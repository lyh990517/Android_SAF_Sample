package com.rsupport.saftest.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.rsupport.saftest.model.Route


@Composable
fun HomeScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navHostController.navigate(Route.SAF_EXPLORER) }) {
            Text(text = Route.SAF_EXPLORER)
        }
        BackHandler {
            navHostController.popBackStack()
        }
    }
}