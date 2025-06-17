package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class SunEvents(val sunriseTime: String? = null, val sunsetTime: String? = null)