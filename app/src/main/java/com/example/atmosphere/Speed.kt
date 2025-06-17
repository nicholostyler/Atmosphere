package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Speed(val value: Long? = null, val unit: String? = null)