package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCondition(
    val iconBaseUri: String? = null,
    val description: Description? = null,
    val type: String? = null
)