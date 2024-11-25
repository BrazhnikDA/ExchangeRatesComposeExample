package ru.exchangerates.data.remote.dto.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.exchangerates.domain.model.CurrencyDetails
import ru.exchangerates.domain.model.CurrencyInfo
import ru.exchangerates.domain.model.LatestRates

@JsonClass(generateAdapter = true)
data class LatestRatesDto(
    @Json(name = "Date")
    val date: String,
    @Json(name = "PreviousDate")
    val previousDate: String,
    @Json(name = "PreviousURL")
    val previousUrl: String,
    @Json(name = "Timestamp")
    val timestamp: String,
    @Json(name = "Valute")
    val valute: Map<String, CurrencyDto>
) {
    fun toModel(): LatestRates {
        return LatestRates(
            date = this.date,
            previousDate = this.previousDate,
            previousUrl = this.previousUrl,
            timestamp = this.timestamp,
            valute = this.valute.map { (key, value) ->
                CurrencyInfo(
                    name = key,
                    details = value.toModel(),
                )
            },
        )
    }
}

@JsonClass(generateAdapter = true)
data class CurrencyDto(
    @Json(name = "ID")
    val id: String,
    @Json(name = "NumCode")
    val numCode: String,
    @Json(name = "CharCode")
    val charCode: String,
    @Json(name = "Nominal")
    val nominal: Int,
    @Json(name = "Name")
    val name: String,
    @Json(name = "Value")
    val value: Double,
    @Json(name = "Previous")
    val previous: Double
) {
    fun toModel(): CurrencyDetails {
        return CurrencyDetails(
            id = this.id,
            numCode = this.numCode,
            charCode = this.charCode,
            nominal = this.nominal,
            name = this.name,
            value = this.value,
            previous = this.previous,
        )
    }
}