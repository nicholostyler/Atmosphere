package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Interval(val startTime: String? = null, val endTime: String? = null)