package com.example.fastik.ui.theme


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fastik.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.ImeAction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(viewModel: MainViewModel = viewModel())
{
    val searchState = rememberTextFieldState()
    val searchResults = remember { mutableStateListOf("Metallica", "AC/DC", "Rammstein","вышел покурить","this fffire","твойночнойкошмарвгрязи") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {SearchBar(
            textFieldState = searchState,
            searchResults = searchResults,
            onSearch = { query ->
                println("Ищем: $query")
                // Здесь обычно вызывают метод из viewModel
            },
            onClearHistory = { searchResults.clear() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )})
    { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding))
        {
            Text("Тут будет контент плеера")
        }
    }

}

@Preview
@Composable
fun ScreenPreview()
{
    Screen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    onClearHistory: () -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }


    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier.height(56.dp),
                query = textFieldState.text.toString(),
                onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                onSearch = {
                    onSearch(textFieldState.text.toString())
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search",style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null,modifier = Modifier.size(20.dp))},
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(onClick = {
                            textFieldState.clearText() // Очищает строку
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        colors = SearchBarDefaults.colors(
            dividerColor = Color.Transparent,
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {

        Column(Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp), // Отступ от заголовка до чипсов
                horizontalArrangement = Arrangement.SpaceBetween, // Разносит текст и иконку по краям
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "История поиска",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Кнопка очистки истории
                if (searchResults.isNotEmpty()) {
                    IconButton(onClick = { onClearHistory() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Очистить историю",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            androidx.compose.foundation.layout.FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                searchResults.forEach { historyItem ->
                    SuggestionChip(
                        onClick = {
                            textFieldState.edit { replace(0, length, historyItem) }
                            onSearch(historyItem)
                            expanded = false
                        },
                        label = { Text(historyItem) },
                        shape = CircleShape
                    )
                }
            }
        }
    }
}

