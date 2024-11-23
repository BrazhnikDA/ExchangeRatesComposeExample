package ru.exchangerates.domain.model

import java.util.Date

data class ExchangeRates(
    val disclaimer: String,
    val license: String,
    val timestamp: Date, // Преобразование timestamp в Date
    val base: String,
    val rates: List<CurrencyRate> // Преобразование Map в List
)

data class CurrencyRate(val currency: String, val rate: Double)