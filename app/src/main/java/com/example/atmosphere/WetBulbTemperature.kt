package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class WetBulbTemperature(val unit: String? = null, val degrees: Double? = null)