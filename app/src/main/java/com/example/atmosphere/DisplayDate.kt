package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class DisplayDate(val year: Long? = null, val month: Long? = null, val day: Long? = null)