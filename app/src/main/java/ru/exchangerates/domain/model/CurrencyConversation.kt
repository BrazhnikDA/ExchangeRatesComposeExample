package ru.exchangerates.domain.model

import com.squareup.moshi.Json

data class CurrencyConversation(
    val baseCode: String,
    val targetCode: String,
    val conversionRate: Double,
)
