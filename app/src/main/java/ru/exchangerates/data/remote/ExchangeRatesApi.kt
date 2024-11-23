package ru.exchangerates.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesApi {

    @GET("latest.json")
    suspend fun getLatestRates(
        @Query("app_id") appId: String,
        @Query("base") base: String? = null, // Base currency (optional)
        @Query("symbols") symbols: String? = null // Comma-separated list of symbols (optional)
    ): Response<ExchangeRatesDto>

    companion object {
        internal const val BASE_URL = "https://openexchangerates.org/api/"

        // !!!NON SAFETY!!!
        // ------------------------------------------------------------------
        internal const val PRIVATE_KEY = "01007859154243c09e489d9e55510157"
        // ------------------------------------------------------------------
    }
}