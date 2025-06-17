package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Direction(val degrees: Int? = null, val cardinal: String? = null)