package ru.exchangerates.ui.mainscreen

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import ru.exchangerates.domain.repository.ExchangeRateRepository
import javax.inject.Inject

@HiltViewModel
internal class MainScreenViewModel @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val dispatcher: MainCoroutineDispatcher = Dispatchers.Main.immediate,
) {

    private val _uiState = MutableStateFlow(MainScreenUiState(loading = true))
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(dispatcher) {
            exchangeRateRepository.getLatestRates().map { result ->

            }
        }
    }
}