package com.vito.tiprankshometask.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vito.tiprankshometask.viewmodel.StockViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: StockViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedStock by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(horizontal = 16.dp)
    ) {
        SearchBar(
            value = inputText.text,
            onValueChange = {
                inputText = TextFieldValue(it)
                viewModel.fetchAutoComplete(it)
            },
            onSearch = {
                if (inputText.text.isBlank()) {
                    viewModel.fetchStocksResults("M")
                } else {
                    viewModel.fetchStocksResults(inputText.text)
                }
                viewModel.clearSuggestions()
            },
            suggestions = uiState.suggestions,
            onSuggestionClick = { suggestion ->
                inputText = TextFieldValue(suggestion.value)
                viewModel.fetchStocksResults(suggestion.value)
                viewModel.clearSuggestions()
            }
        )

        Spacer(Modifier.height(12.dp))

        Box(modifier = Modifier.weight(1f)) {
            when {
                uiState.loading -> LoaderOverlay()
                uiState.error != null -> ErrorCard(
                    message = uiState.error ?: "",
                    onRetry = { viewModel.fetchStocksResults("M") }
                )
                else -> StockGrid(
                    stocks = uiState.stocks,
                    onUidClick = { uid ->
                        Toast.makeText(context, uid, Toast.LENGTH_SHORT).show()
                    },
                    onCardClick = { index, label ->
                        Toast.makeText(context, "Stock #${index + 1}: $label", Toast.LENGTH_SHORT).show()
                    },
                    selectedStockIndex = selectedStock ?: -1,
                    onLongClick = { index ->
                             selectedStock = if (selectedStock == index) null else index
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

