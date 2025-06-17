package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class DewPoint(val unit: String? = null, val degrees: Double? = null)