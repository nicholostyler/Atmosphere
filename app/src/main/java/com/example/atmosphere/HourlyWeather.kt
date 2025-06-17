package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeather(
    val forecastHours: List<ForecastHour>? = null,
    val timeZone: TimeZone? = null,
    val nextPageToken: String? = null,
)