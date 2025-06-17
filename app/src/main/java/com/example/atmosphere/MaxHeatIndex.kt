package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class MaxHeatIndex(val degrees: Double? = null, val unit: String? = null)