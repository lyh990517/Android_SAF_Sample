package com.rsupport.saftest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rsupport.saftest.ui.theme.SaftestTheme
import com.rsupport.saftest.util.JsonDebugTree
import com.rsupport.saftest.util.Util
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(JsonDebugTree(Util.logCollector))
        setContent {
            SaftestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FileExplorerApp()
                }
            }
        }
    }
}
