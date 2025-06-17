package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class DisplayDateTime(
    val year: Long? = null,
    val month: Long? = null,
    val day: Long? = null,
    val hours: Long? = null,
    val minutes: Long? = null,
    val seconds: Long? = null,
    val nanos: Long? = null,
    val utcOffset: String? = null,
)