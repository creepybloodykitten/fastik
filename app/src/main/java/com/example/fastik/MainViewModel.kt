package com.example.fastik

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModelProvider

class MainViewModel(private val repository: SongRepository) : ViewModel()
{
    val songs = mutableStateListOf<Song>()

    fun loadSongs(folderName: String) {
        val fetchedSongs = repository.fetchSongsFromFolder(folderName)
        songs.clear()
        songs.addAll(fetchedSongs)
    }
}

class MainViewModelFactory(
    private val repository: SongRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}