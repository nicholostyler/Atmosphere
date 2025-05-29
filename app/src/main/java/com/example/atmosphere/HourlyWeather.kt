package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeather(
    val forecastHours: List<ForecastHour>,
    val timeZone: TimeZone,
    val nextPageToken: String,
)
@Serializable

data class ForecastHour(
    val interval: Interval,
    val displayDateTime: DisplayDateTime,
    val weatherCondition: WeatherCondition,
    val temperature: Temperature,
    val feelsLikeTemperature: FeelsLikeTemperature,
    val dewPoint: DewPoint,
    val heatIndex: HeatIndex,
    val windChill: WindChill,
    val wetBulbTemperature: WetBulbTemperature,
    val precipitation: Precipitation,
    val airPressure: AirPressure,
    val wind: Wind,
    val visibility: Visibility,
    val iceThickness: IceThickness,
    val isDaytime: Boolean,
    val relativeHumidity: Long,
    val uvIndex: Long,
    val thunderstormProbability: Long,
    val cloudCover: Long,
)
@Serializable

data class DisplayDateTime(
    val year: Long,
    val month: Long,
    val day: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
    val nanos: Long,
    val utcOffset: String,
)
@Serializable

data class Temperature(
    val unit: String,
    val degrees: Double,
)
@Serializable

data class FeelsLikeTemperature(
    val unit: String,
    val degrees: Double,
)
@Serializable

data class DewPoint(
    val unit: String,
    val degrees: Double,
)
@Serializable

data class HeatIndex(
    val unit: String,
    val degrees: Double,
)
@Serializable

data class WindChill(
    val unit: String,
    val degrees: Double,
)
@Serializable

data class WetBulbTemperature(
    val unit: String,
    val degrees: Double,
)
@Serializable

data class AirPressure(
    val meanSeaLevelMillibars: Double,
)

@Serializable

data class Visibility(
    val unit: String,
    val distance: Long,
)
