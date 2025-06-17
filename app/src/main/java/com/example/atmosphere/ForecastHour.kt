package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class ForecastHour(
    val interval: Interval? = null,
    val displayDateTime: DisplayDateTime? = null,
    val weatherCondition: WeatherCondition? = null,
    val temperature: Temperature? = null,
    val feelsLikeTemperature: FeelsLikeTemperature? = null,
    val dewPoint: DewPoint? = null,
    val heatIndex: HeatIndex? = null,
    val windChill: WindChill? = null,
    val wetBulbTemperature: WetBulbTemperature? = null,
    val precipitation: Precipitation? = null,
    val airPressure: AirPressure? = null,
    val wind: Wind? = null,
    val visibility: Visibility? = null,
    val iceThickness: IceThickness? = null,
    val isDaytime: Boolean? = null,
    val relativeHumidity: Long? = null,
    val uvIndex: Long? = null,
    val thunderstormProbability: Long? = null,
    val cloudCover: Long? = null,
)