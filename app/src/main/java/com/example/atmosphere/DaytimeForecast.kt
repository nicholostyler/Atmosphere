package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class DaytimeForecast(
    val interval: DaytimeInterval? = null,
    val weatherCondition: WeatherCondition? = null,
    val relativeHumidity: Long? = null,
    val uvIndex: Long? = null,
    val precipitation: Precipitation? = null,
    val thunderstormProbability: Long? = null,
    val wind: Wind? = null,
    val cloudCover: Long? = null,
    val iceThickness: IceThickness? = null,
)