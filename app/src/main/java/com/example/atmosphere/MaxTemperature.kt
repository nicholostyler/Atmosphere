package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class MaxTemperature(val degrees: Double? = null, val unit: String? = null)