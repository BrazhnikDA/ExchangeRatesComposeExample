package ru.exchangerates.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.exchangerates.data.remote.dto.xml.ValCursDto

interface HistoryPeriodApi {

    @GET("XML_dynamic.asp")
    suspend fun getCurrencyHistoryPeriod(
        @Query("date_req1") startDate: String,
        @Query("date_req2") endDate: String,
        @Query("VAL_NM_RQ") currencyId: String
    ): ValCursDto

    companion object {
        internal const val BASE_URL_CB_RF_SCRIPT = "https://www.cbr.ru/scripts/"
    }
}