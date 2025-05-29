package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class DailyWeather(
    val forecastDays: List<ForecastDay>,
    val timeZone: TimeZone,
    val nextPageToken: String? = null,
)

@Serializable
data class ForecastDay(
    val interval: Interval,
    val displayDate: DisplayDate,
    val daytimeForecast: DaytimeForecast,
    val nighttimeForecast: NighttimeForecast,
    val maxTemperature: MaxTemperature,
    val minTemperature: MinTemperature,
    val feelsLikeMaxTemperature: FeelsLikeMaxTemperature,
    val feelsLikeMinTemperature: FeelsLikeMinTemperature,
    val sunEvents: SunEvents,
    val moonEvents: MoonEvents,
    val maxHeatIndex: MaxHeatIndex,
)
@Serializable
data class Interval(
    val startTime: String,
    val endTime: String,
)
@Serializable

data class DisplayDate(
    val year: Long,
    val month: Long,
    val day: Long,
)
@Serializable

data class DaytimeForecast(
    val interval: DaytimeInterval,
    val weatherCondition: WeatherCondition,
    val relativeHumidity: Long,
    val uvIndex: Long,
    val precipitation: Precipitation,
    val thunderstormProbability: Long,
    val wind: Wind,
    val cloudCover: Long,
    val iceThickness: IceThickness,
)
@Serializable

data class DaytimeInterval(
    val startTime: String,
    val endTime: String,
)
@Serializable

data class WeatherCondition(
    val iconBaseUri: String,
    val description: Description,
    val type: String,
)
@Serializable

data class Description(
    val text: String,
    val languageCode: String,
)
@Serializable

data class Precipitation(
    val probability: Probability,
    val snowQpf: SnowQpf,
    val qpf: Qpf,
)
@Serializable

data class Probability(
    val percent: Long,
    val type: String,
)
@Serializable

data class SnowQpf(
    val quantity: Long,
    val unit: String,
)
@Serializable

data class Qpf(
    val quantity: Double,
    val unit: String,
)
@Serializable

data class Wind(
    val direction: Direction,
    val speed: Speed,
    val gust: Gust,
)
@Serializable

data class Direction(
    val degrees: Long,
    val cardinal: String,
)
@Serializable

data class Speed(
    val value: Long,
    val unit: String,
)
@Serializable

data class Gust(
    val value: Long,
    val unit: String,
)
@Serializable

data class IceThickness(
    val thickness: Long,
    val unit: String,
)
@Serializable

data class NighttimeForecast(
    val interval: Interval,
    val weatherCondition: WeatherCondition,
    val relativeHumidity: Long,
    val uvIndex: Long,
    val precipitation: Precipitation,
    val thunderstormProbability: Long,
    val wind: Wind,
    val cloudCover: Long,
    val iceThickness: IceThickness,
)

data class NighttimeInterval(
    val startTime: String,
    val endTime: String,
)

data class NighttimeWeatherCondition(
    val iconBaseUri: String,
    val description: Description,
    val type: String,
)

data class NighttimeWeatherConditionDescription(
    val text: String,
    val languageCode: String,
)

data class NighttimePrecipitation(
    val probability: Probability,
    val snowQpf: SnowQpf,
    val qpf: Qpf,
)

data class NighttimePrecipitationProbability(
    val percent: Long,
    val type: String,
)

data class NighttimePrecipitationSnowQpf(
    val quantity: Long,
    val unit: String,
)

data class NighttimePrecipitationqpf(
    val quantity: Long,
    val unit: String,
)

data class NighttimeWind(
    val direction: Direction,
    val speed: Speed,
    val gust: Gust,
)

data class NighttimeWindDirection(
    val degrees: Long,
    val cardinal: String,
)

data class NighttimeWindSpeed(
    val value: Long,
    val unit: String,
)

data class NighttimeWindGust(
    val value: Long,
    val unit: String,
)

data class IceThickness2(
    val thickness: Long,
    val unit: String,
)
@Serializable

data class MaxTemperature(
    val degrees: Double,
    val unit: String,
)
@Serializable

data class MinTemperature(
    val degrees: Double,
    val unit: String,
)
@Serializable

data class FeelsLikeMaxTemperature(
    val degrees: Double,
    val unit: String,
)
@Serializable

data class FeelsLikeMinTemperature(
    val degrees: Double,
    val unit: String,
)
@Serializable

data class SunEvents(
    val sunriseTime: String,
    val sunsetTime: String,
)
@Serializable

data class MoonEvents(
    val moonPhase: String,
    val moonriseTimes: List<String>,
    val moonsetTimes: List<String>? = null,
)
@Serializable

data class MaxHeatIndex(
    val degrees: Double,
    val unit: String,
)
@Serializable

data class TimeZone(
    val id: String,
)
