package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class CurrentConditionsHistory(
    val temperatureChange: Temperature? = null,
    val maxTemperature: Temperature? = null,
    val minTemperature: Temperature? = null,
    val snowQpf: QuantityUnit? = null,
    val qpf: QuantityUnit? = null
)