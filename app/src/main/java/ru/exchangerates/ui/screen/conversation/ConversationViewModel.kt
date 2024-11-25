package ru.exchangerates.ui.screen.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.exchangerates.domain.repository.CurrencyConversationRepository
import javax.inject.Inject

@HiltViewModel
internal class ConversationViewModel @Inject constructor(
    private val currencyConversationRepository: CurrencyConversationRepository,
) : ViewModel() {
    private val dispatcher: MainCoroutineDispatcher = Dispatchers.Main.immediate

    private var _uiState: MutableStateFlow<ConversationUiState> =
        MutableStateFlow(ConversationUiState.Default)
    internal val uiState: StateFlow<ConversationUiState> = _uiState.asStateFlow()

    internal fun startConversation(fromCurrency: String, toCurrency: String) {
        _uiState.value = ConversationUiState.Loading
        viewModelScope.launch(dispatcher) {
            currencyConversationRepository.getConvertCurrency(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
            ).onSuccess { result ->
                val model = result.toModel()
                _uiState.value = ConversationUiState.Success(ConversationInfo(model))
            }.onFailure { exception ->
                val code = (exception as HttpException).code()
                if (code == 404) {
                    _uiState.value = ConversationUiState.ErrorNotSupported
                } else {
                    _uiState.value = ConversationUiState.Error
                }
            }
        }
    }
}