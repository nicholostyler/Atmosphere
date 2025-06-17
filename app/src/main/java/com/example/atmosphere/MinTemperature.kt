package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class MinTemperature(val degrees: Double? = null, val unit: String? = null)