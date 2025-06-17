package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class DaytimeInterval(val startTime: String? = null, val endTime: String? = null)