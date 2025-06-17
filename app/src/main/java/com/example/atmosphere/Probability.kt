package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Probability(val percent: Int? = null, val type: String? = null)