package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class AirPressure(val meanSeaLevelMillibars: Double? = null)