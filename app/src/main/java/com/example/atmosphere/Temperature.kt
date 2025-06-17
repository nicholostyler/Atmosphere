package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Temperature(val degrees: Double? = null, val unit: String? = null)