package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class QuantityUnit(val quantity: Double? = null, val unit: String? = null)