package com.example.fastik.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fastik.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(viewModel: MainViewModel = viewModel())
{
    val searchState = rememberTextFieldState()
    val searchResults = remember { mutableStateListOf("Metallica", "AC/DC", "Rammstein","вышел покурить","this fffire","твойночнойкошмарвгрязи") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar(
            textFieldState = searchState,
            searchResults = searchResults,
            onSearch = { query ->
                println("Ищем: $query")

            },
            onClearHistory = { searchResults.clear() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )}
    )
    { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding))
        {
            SongListScreen(songs = viewModel.songs)
        }
    }

}

@Preview
@Composable
fun ScreenPreview()
{
    Screen()
}






