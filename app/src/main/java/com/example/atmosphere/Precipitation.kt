package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Precipitation(
    val probability: Probability? = null,
    val snowQpf: QuantityUnit? = null,
    val qpf: QuantityUnit? = null
)