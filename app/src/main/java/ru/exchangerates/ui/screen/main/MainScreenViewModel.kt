package ru.exchangerates.ui.screen.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import ru.exchangerates.domain.repository.LatestRatesRepository
import javax.inject.Inject

@HiltViewModel
internal class MainScreenViewModel @Inject constructor(
    private val latestRatesRepository: LatestRatesRepository,
) : ViewModel() {
    private val dispatcher: MainCoroutineDispatcher = Dispatchers.Main.immediate

    private var _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState.Loading)
    internal val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(dispatcher) {
            latestRatesRepository.getLatestRates()
                .onSuccess { result ->
                    val model = result.toModel()
                    _uiState.value = MainScreenUiState.Success(MainScreenInfo(model))
                }
                .onFailure {
                    _uiState.value = MainScreenUiState.Error
                }
        }
    }
}