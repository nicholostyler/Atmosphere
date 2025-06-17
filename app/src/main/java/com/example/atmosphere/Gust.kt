package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Gust(val value: Long? = null, val unit: String? = null)