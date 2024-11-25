package ru.exchangerates.ui.screen.conversation

import ru.exchangerates.domain.model.CurrencyConversation

internal sealed class ConversationUiState {

    data object Loading : ConversationUiState()

    data object Default : ConversationUiState()

    data object ErrorNotSupported : ConversationUiState()

    data object Error : ConversationUiState()

    data class Success(val value: ConversationInfo) : ConversationUiState()
}

internal data class ConversationInfo(
    val result: CurrencyConversation,
)