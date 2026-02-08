package com.example.fastik.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    onClearHistory: () -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(expanded) {
        if (!expanded) {
            focusManager.clearFocus()
        }
    }

    val animatedInputFieldColor by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceContainerHigh,
        animationSpec = tween(durationMillis = 300), // Чуть быстрее, чтобы успеть за расширением
        label = "InputFieldColorAnimation"
    )

    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .semantics { traversalIndex = 0f },
        inputField = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        color = animatedInputFieldColor,
                        shape = CircleShape // Всегда круглый
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    state = textFieldState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { if (it.isFocused) expanded = true },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    onKeyboardAction = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                        focusManager.clearFocus()
                    },
                    decorator = { innerTextField ->
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(modifier = Modifier.padding(start = 12.dp, end = 8.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                if (textFieldState.text.isEmpty()) {
                                    Text(
                                        "Search",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                            if (textFieldState.text.isNotEmpty()) {
                                IconButton(onClick = { textFieldState.clearText() }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        expanded = expanded,
        onExpandedChange = {
            expanded = it
            if (!it) focusManager.clearFocus()
        },
        colors = SearchBarDefaults.colors(
            dividerColor = Color.Transparent,
            containerColor = MaterialTheme.colorScheme.background,
        ),
        tonalElevation = 0.dp
    ) {
        // Контент (история)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "История поиска",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (searchResults.isNotEmpty()) {
                    IconButton(onClick = { onClearHistory() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Очистить",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                searchResults.forEach { historyItem ->
                    SuggestionChip(
                        onClick = {
                            textFieldState.edit { replace(0, length, historyItem) }
                            onSearch(historyItem)
                            expanded = false
                            focusManager.clearFocus()
                        },
                        label = { Text(historyItem) },
                        shape = CircleShape
                    )
                }
            }
        }
    }
}


//базовый вариант material3
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar(
//    textFieldState: TextFieldState,
//    onSearch: (String) -> Unit,
//    onClearHistory: () -> Unit,
//    searchResults: List<String>,
//    modifier: Modifier = Modifier
//) {
//    // Controls expansion state of the search bar
//    var expanded by rememberSaveable { mutableStateOf(false) }
//
//
//    SearchBar(
//        modifier = modifier
//            .fillMaxWidth()
//            .semantics { traversalIndex = 0f },
//        inputField = {
//            SearchBarDefaults.InputField(
//                modifier = Modifier.height(56.dp),
//                query = textFieldState.text.toString(),
//                onQueryChange = { textFieldState.edit { replace(0, length, it) } },
//                onSearch = {
//                    onSearch(textFieldState.text.toString())
//                    expanded = false
//                },
//                expanded = expanded,
//                onExpandedChange = { expanded = it },
//                placeholder = { Text("Search",style = MaterialTheme.typography.bodyMedium) },
//                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null,modifier = Modifier.size(20.dp))},
//                trailingIcon = {
//                    if (textFieldState.text.isNotEmpty()) {
//                        IconButton(onClick = {
//                            textFieldState.clearText()
//                        }) {
//                            Icon(Icons.Default.Clear, contentDescription = "Clear")
//                        }
//                    }
//                }
//            )
//        },
//        expanded = expanded,
//        onExpandedChange = { expanded = it },
//        colors = SearchBarDefaults.colors(
//            dividerColor = Color.Transparent,
//            containerColor = MaterialTheme.colorScheme.background,
//        )
//    ) {
//
//        Column(Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 8.dp), // Отступ от заголовка до чипсов
//                horizontalArrangement = Arrangement.SpaceBetween, // Разносит текст и иконку по краям
//                verticalAlignment = Alignment.CenterVertically
//            ){
//                Text(
//                    text = "История поиска",
//                    style = MaterialTheme.typography.titleSmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//                // Кнопка очистки истории
//                if (searchResults.isNotEmpty()) {
//                    IconButton(onClick = { onClearHistory() }) {
//                        Icon(
//                            imageVector = Icons.Default.Delete,
//                            contentDescription = "Очистить историю",
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                }
//            }
//            androidx.compose.foundation.layout.FlowRow(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalArrangement = Arrangement.spacedBy(4.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                searchResults.forEach { historyItem ->
//                    SuggestionChip(
//                        onClick = {
//                            textFieldState.edit { replace(0, length, historyItem) }
//                            onSearch(historyItem)
//                            expanded = false
//                        },
//                        label = { Text(historyItem) },
//                        shape = CircleShape
//                    )
//                }
//            }
//        }
//    }
//}