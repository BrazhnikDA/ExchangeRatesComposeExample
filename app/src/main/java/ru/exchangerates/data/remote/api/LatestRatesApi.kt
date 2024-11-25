package ru.exchangerates.data.remote.api

import retrofit2.http.GET
import ru.exchangerates.data.remote.dto.json.LatestRatesDto

interface LatestRatesApi {

    @GET("daily_json.js")
    suspend fun getLatestRates(): LatestRatesDto

    companion object {
        internal const val BASE_URL_CB_RF_DAILY = "https://www.cbr-xml-daily.ru/"
    }
}