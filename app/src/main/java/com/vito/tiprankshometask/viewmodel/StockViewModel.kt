package com.vito.tiprankshometask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.tiprankshometask.remote.StockItem
import com.vito.tiprankshometask.remote.StockRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StockViewModel(
    private val repository: StockRepository = StockRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private var job: Job? = null

    init {
        fetchInitialStocks()
    }

    fun fetchInitialStocks() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            val result = repository.fetchStocks("M")
            result
                .onSuccess { list ->
                    val sorted = list.sortedBy { it.label }.take(9)
                    _uiState.update { it.copy(stocks = sorted, loading = false, error = null) }
                }
                .onFailure { err ->
                    _uiState.update { it.copy(loading = false, error = err.localizedMessage ?: "Unknown error") }
                }
        }
    }

    fun fetchStocksResults(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            val result = repository.fetchStocks(query)
            result
                .onSuccess { list ->
                    val sorted = list.sortedBy { it.label }
                    _uiState.update { it.copy(stocks = sorted, loading = false, error = null) }
                }
                .onFailure { err ->
                    _uiState.update { it.copy(loading = false, error = err.localizedMessage ?: "Unknown error") }
                }
        }
    }

    fun fetchAutoComplete(query: String) {
        job?.cancel()
        if (query.isEmpty()) {
            _uiState.update { it.copy(suggestions = emptyList()) }
            return
        }
        job = viewModelScope.launch {
            delay(300)
            val result = repository.fetchStocks(query)
            result.onSuccess { list ->
                val sorted = list.sortedBy { it.label }
                _uiState.update { it.copy(suggestions = sorted) }
            }.onFailure {
                _uiState.update { it.copy(suggestions = emptyList()) }
            }
        }
    }

    fun clearSuggestions() {
        _uiState.update { it.copy(suggestions = emptyList()) }
    }
}

data class UiState(
    val stocks: List<StockItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val suggestions: List<StockItem> = emptyList()
)