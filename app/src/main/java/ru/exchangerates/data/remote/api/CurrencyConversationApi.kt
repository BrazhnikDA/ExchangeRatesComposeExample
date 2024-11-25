package ru.exchangerates.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.exchangerates.data.remote.dto.json.CurrencyConversationDto

interface CurrencyConversationApi {

    @GET("v6/{apiKey}/pair/{from}/{to}")
    suspend fun getConvertCurrency(
        @Path("apiKey") apiKey: String,
        @Path("from") fromCurrency: String,
        @Path("to") toCurrency: String
    ): CurrencyConversationDto

    companion object {
        internal const val BASE_URL_EXCHANGE_RATE = "https://v6.exchangerate-api.com/"

        // !!!NON SAFETY!!!
        // ------------------------------------------------------------------
        internal const val PRIVATE_KEY_EXCHANGE_RATE = "1e77103f458b25cb87a31a4e"
        // ------------------------------------------------------------------
    }
}