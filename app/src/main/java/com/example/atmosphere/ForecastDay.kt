package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDay(
    val interval: Interval? = null,
    val displayDate: DisplayDate? = null,
    val daytimeForecast: DaytimeForecast? = null,
    val nighttimeForecast: NighttimeForecast? = null,
    val maxTemperature: MaxTemperature? = null,
    val minTemperature: MinTemperature? = null,
    val feelsLikeMaxTemperature: FeelsLikeMaxTemperature? = null,
    val feelsLikeMinTemperature: FeelsLikeMinTemperature? = null,
    val sunEvents: SunEvents? = null,
    val moonEvents: MoonEvents? = null,
    val maxHeatIndex: MaxHeatIndex? = null,
)