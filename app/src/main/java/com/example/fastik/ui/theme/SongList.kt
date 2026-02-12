package com.example.fastik.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.fastik.Song
import androidx.core.net.toUri
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun SongListScreen(songs: List<Song>) {
    LazyColumn(modifier = Modifier.fillMaxSize(),
        // Оптимизация скролла (если список большой)
        contentPadding = PaddingValues(bottom = 16.dp)) {

        items(items = songs, key = { it.id }) { song ->
            SongItem(
                song = song,
                onItemClick = { /* Логика проигрывания */ },
                onMenuClick = { /* Логика открытия меню */ }
            )
        }
    }
}

@Composable
fun SongItem(
    song: Song,
    onItemClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val context = LocalContext.current //что это

    // Формируем URI для обложки альбома
    val artworkUri = remember(song.albumId) {
        ContentUris.withAppendedId(
            "content://media/external/audio/albumart".toUri(),
            song.albumId
        )
    }

    val artistAndDuration = remember(song.artist, song.duration) {
        "${song.artist} ◦ ${formatDuration(song.duration)}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth().clickable { onItemClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        // Обложка
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(artworkUri)
                .crossfade(true)
                .build(),
            contentDescription = "Album Art",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp) // Уменьшили размер (было 50, стандарт часто 40-48)
                .clip(RoundedCornerShape(8.dp)) // Закругление углов (можно 12.dp для большей круглости)
                .background(MaterialTheme.colorScheme.surfaceVariant), // Фон, пока грузится
            error = rememberVectorPainter(Icons.Default.MusicNote) // Вектор вместо загрузки из интернета!
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal, // Убираем жирность
                    fontSize = 16.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 3) Расстояние поменьше: Spacer не нужен, если использовать lineHeight или просто Text друг за другом
            // 5) Длительность вместе с автором через символ ◦
            Text(
                text = artistAndDuration,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Более тусклый цвет
                modifier = Modifier.padding(top = 2.dp) // Минимальный отступ от названия
            )
        }

        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(40.dp) // Чуть меньше стандартного тач-таргета для компактности списка
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Song Options",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


fun formatDuration(ms: Long): String {
    val minutes = (ms / 1000) / 60
    val seconds = (ms / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}