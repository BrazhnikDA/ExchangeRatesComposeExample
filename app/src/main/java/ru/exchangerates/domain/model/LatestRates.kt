package ru.exchangerates.domain.model

import java.time.OffsetDateTime

data class LatestRates(
    val date: String,
    val previousDate: String,
    val previousUrl: String,
    val timestamp: String,
    val valute: List<CurrencyInfo>
)

data class CurrencyInfo(
    val name: String,
    val details: CurrencyDetails,
)

data class CurrencyDetails(
    val id: String,
    val numCode: String,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val previous: Double
)
