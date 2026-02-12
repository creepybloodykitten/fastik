package com.example.fastik

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.fastik.ui.theme.FastikTheme
import com.example.fastik.ui.theme.Screen

class MainActivity : ComponentActivity() {
    // создание репозитория и вьюмодель
    private val repository by lazy { SongRepository(this) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(repository)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadSongs("vmpdownloadedsongs/зимняя атмосфера")
        } else {
            // Тут можно вывести текст: "Эх, без доступа к файлам музыки не будет..."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                // Разрешение уже дано ранее! Просто грузим музыку.
                viewModel.loadSongs("vmpdownloadedsongs/зимняя атмосфера")
            }
            else -> {
                // Разрешения нет. Спрашиваем первый (или не первый) раз.
                requestPermissionLauncher.launch(permission)
            }
        }
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

