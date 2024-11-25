package ru.exchangerates.domain.repository

import ru.exchangerates.data.remote.api.CurrencyConversationApi.Companion.PRIVATE_KEY_EXCHANGE_RATE
import ru.exchangerates.data.remote.dto.json.CurrencyConversationDto

interface CurrencyConversationRepository {

    suspend fun getConvertCurrency(
        apiKey: String = PRIVATE_KEY_EXCHANGE_RATE,
        fromCurrency: String,
        toCurrency: String,
    ): Result<CurrencyConversationDto>
}