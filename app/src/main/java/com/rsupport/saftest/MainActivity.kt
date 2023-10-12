package com.rsupport.saftest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rsupport.saftest.util.Constants.HOME_DIR
import com.rsupport.saftest.ui.theme.SaftestTheme
import com.rsupport.saftest.util.JsonDebugTree
import timber.log.Timber
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("dir",HOME_DIR)
        Timber.plant(JsonDebugTree())
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
