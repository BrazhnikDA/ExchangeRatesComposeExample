package ru.exchangerates.data.remote.dto.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.exchangerates.domain.model.CurrencyConversation

@JsonClass(generateAdapter = true)
data class CurrencyConversationDto(
    @Json(name = "result")
    val result: String,
    @Json(name = "documentation")
    val documentation: String,
    @Json(name = "terms_of_use")
    val termsOfUse: String,
    @Json(name = "time_last_update_unix")
    val timeLastUpdateUnix: Long,
    @Json(name = "time_last_update_utc")
    val timeLastUpdateUtc: String,
    @Json(name = "time_next_update_unix")
    val timeNextUpdateUnix: Long,
    @Json(name = "time_next_update_utc")
    val timeNextUpdateUtc: String,
    @Json(name = "base_code")
    val baseCode: String,
    @Json(name = "target_code")
    val targetCode: String,
    @Json(name = "conversion_rate")
    val conversionRate: Double
) {
    fun toModel(): CurrencyConversation {
        return CurrencyConversation(
            baseCode = this.baseCode,
            targetCode = this.targetCode,
            conversionRate = this.conversionRate,
        )
    }
}