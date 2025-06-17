package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class ValueUnit(val value: Int? = null, val unit: String? = null)