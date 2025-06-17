package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Qpf(val quantity: Double? = null, val unit: String? = null)