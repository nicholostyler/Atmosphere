package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class DailyWeather(
    val forecastDays: List<ForecastDay>? = null,
    val timeZone: TimeZone? = null,
    val nextPageToken: String? = null,
)