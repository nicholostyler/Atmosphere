package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val direction: Direction? = null,
    val speed: ValueUnit? = null,
    val gust: ValueUnit? = null
)