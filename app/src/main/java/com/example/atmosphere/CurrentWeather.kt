import kotlinx.serialization.Serializable

// Your CurrentWeather.kt data classes:

@Serializable
data class CurrentWeather(
    val currentTime: String,
    val timeZone: TimeZone, // Matches
    val isDaytime: Boolean, // Matches
    val weatherCondition: WeatherCondition, // Matches
    val temperature: Temperature, // Matches
    val feelsLikeTemperature: Temperature, // Matches
    val dewPoint: Temperature, // Matches
    val heatIndex: Temperature, // Matches
    val windChill: Temperature, // Matches
    val relativeHumidity: Int, // Matches
    val uvIndex: Int, // Matches
    val precipitation: Precipitation, // Matches
    val thunderstormProbability: Int, // Matches
    val airPressure: AirPressure, // Matches
    val wind: Wind, // Matches
    val visibility: Visibility, // Matches
    val cloudCover: Int, // Matches
    val currentConditionsHistory: CurrentConditionsHistory // Matches
)

@Serializable
data class TimeZone( // Matches JSON structure
    val id: String
)

@Serializable
data class WeatherCondition( // Matches JSON structure
    val iconBaseUri: String,
    val description: Description,
    val type: String
)

@Serializable
data class Description( // Matches JSON structure
    val text: String,
    val languageCode: String
)

@Serializable
data class Temperature( // Matches JSON structure
    val degrees: Double,
    val unit: String
)

@Serializable
data class Precipitation(
    val probability: Probability,
    val snowQpf: QuantityUnit,
    val qpf: QuantityUnit
)

@Serializable
data class Probability( // Matches JSON structure
    val percent: Int,
    val type: String
)

@Serializable
data class QuantityUnit( // Matches JSON structure
    val quantity: Double,
    val unit: String
)

@Serializable
data class AirPressure( // Matches JSON structure
    val meanSeaLevelMillibars: Double
)

@Serializable
data class Wind( // Matches JSON structure
    val direction: Direction,
    val speed: ValueUnit,
    val gust: ValueUnit
)

@Serializable
data class Direction( // Matches JSON structure
    val degrees: Int,
    val cardinal: String
)

@Serializable
data class ValueUnit( // Matches JSON structure
    // JSON has "value": 5 (integer)
    val value: Int, // Your model is Int, which is good.
    val unit: String
)

@Serializable
data class Visibility( // Matches JSON structure
    // JSON has "distance": 10 (integer)
    val distance: Int, // Your model is Int, which is good.
    val unit: String
)

@Serializable
data class CurrentConditionsHistory( // Matches JSON structure
    val temperatureChange: Temperature,
    val maxTemperature: Temperature,
    val minTemperature: Temperature,
    val snowQpf: QuantityUnit,
    val qpf: QuantityUnit
)