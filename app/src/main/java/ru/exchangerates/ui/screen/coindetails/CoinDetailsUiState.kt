package ru.exchangerates.ui.screen.coindetails

import ru.exchangerates.data.remote.dto.xml.ValCursDto

internal sealed class CoinDetailsUiState {

    data object Loading : CoinDetailsUiState()

    data object Error : CoinDetailsUiState()

    data class Success(val value: CoinDetailsInfo) : CoinDetailsUiState()
}

internal data class CoinDetailsInfo(
    val historyList: ValCursDto,
)