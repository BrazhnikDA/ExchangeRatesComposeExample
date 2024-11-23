package ru.exchangerates.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.exchangerates.domain.model.CurrencyRate
import ru.exchangerates.domain.model.ExchangeRates
import java.util.Date

@JsonClass(generateAdapter = true)
data class ExchangeRatesDto(
    @Json(name = "disclaimer")
    val disclaimer: String,

    @Json(name = "license")
    val license: String,

    @Json(name = "timestamp")
    val timestamp: Long,

    @Json(name = "base")
    val base: String,

    @Json(name = "rates")
    val rates: Map<String, Double>
) {
    fun ExchangeRatesDto.toModel(): ExchangeRates {
        return ExchangeRates(
            disclaimer = this.disclaimer,
            license = this.license,
            timestamp = Date(this.timestamp * 1000), // Умножаем на 1000 для миллисекунд
            base = this.base,
            rates = this.rates.map { (currency, rate) -> CurrencyRate(currency, rate) }
        )
    }
}