package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class FeelsLikeMinTemperature(val degrees: Double? = null, val unit: String? = null)