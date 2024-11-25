package ru.exchangerates.ui.screen.main

import ru.exchangerates.domain.model.LatestRates

internal sealed class MainScreenUiState {

    data object Loading : MainScreenUiState()

    data object Error : MainScreenUiState()

    data class Success(val value: MainScreenInfo) : MainScreenUiState()
}

internal data class MainScreenInfo(
    val latestRates: LatestRates,
)