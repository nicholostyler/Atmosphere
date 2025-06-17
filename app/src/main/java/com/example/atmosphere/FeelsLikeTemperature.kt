package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class FeelsLikeTemperature(val unit: String? = null, val degrees: Double? = null)