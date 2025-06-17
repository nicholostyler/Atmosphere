package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class MoonEvents(val moonPhase: String? = null, val moonriseTimes: List<String>? = null, val moonsetTimes: List<String>? = null)