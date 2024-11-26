package ru.exchangerates.ui.screen.coindetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.exchangerates.domain.repository.HistoryPeriodRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class CoinDetailsViewModel @Inject constructor(
    private val historyPeriodRepository: HistoryPeriodRepository,
) : ViewModel() {
    private val dispatcher: MainCoroutineDispatcher = Dispatchers.Main.immediate

    private var _uiState: MutableStateFlow<CoinDetailsUiState> =
        MutableStateFlow(CoinDetailsUiState.Loading)
    internal val uiState: StateFlow<CoinDetailsUiState> = _uiState.asStateFlow()

    internal fun firstFetch(currencyId: String) {
        val today = LocalDate.now()
        val result = today.minusDays(10)
        val formattedDateToday =
            today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val formattedDateResult =
            result.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        fetchData(
            formattedDateResult,
            formattedDateToday,
            currencyId,
        )
    }

    internal fun fetchData(
        startDate: String,
        endDate: String,
        currencyId: String
    ) {
        _uiState.value = CoinDetailsUiState.Loading
        viewModelScope.launch(dispatcher) {
            historyPeriodRepository.getHistoryCurrencyPeriod(
                startDate = startDate,
                endDate = endDate,
                currencyId = currencyId,
            ).onSuccess { result ->
                if(result.record != null) {
                    _uiState.value = CoinDetailsUiState.Success(CoinDetailsInfo(result))
                } else {
                    _uiState.value = CoinDetailsUiState.Error
                }
            }.onFailure {
                _uiState.value = CoinDetailsUiState.Error
            }
        }
    }
}