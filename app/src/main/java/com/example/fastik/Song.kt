package com.example.fastik

import android.net.Uri

@androidx.compose.runtime.Immutable
data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val contentUri: Uri,
    val albumId: Long,
    val path: String
)