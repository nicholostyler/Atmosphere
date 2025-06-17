package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    val currentTime: String? = null,
    val timeZone: TimeZone? = null,
    val isDaytime: Boolean? = null,
    val weatherCondition: WeatherCondition? = null,
    val temperature: Temperature? = null,
    val feelsLikeTemperature: Temperature? = null,
    val dewPoint: Temperature? = null,
    val heatIndex: Temperature? = null,
    val windChill: Temperature? = null,
    val relativeHumidity: Int? = null,
    val uvIndex: Int? = null,
    val precipitation: Precipitation? = null,
    val thunderstormProbability: Int? = null,
    val airPressure: AirPressure? = null,
    val wind: Wind? = null,
    val visibility: Visibility? = null,
    val cloudCover: Int? = null,
    val currentConditionsHistory: CurrentConditionsHistory? = null
)