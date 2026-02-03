package com.example.fastik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fastik.ui.theme.FastikTheme
import com.example.fastik.ui.theme.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FastikTheme() {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Screen()
                }
            }
        }
    }
    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("fastiklib")
        }
    }
}

