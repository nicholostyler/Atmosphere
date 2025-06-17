package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class IceThickness(val thickness: Long? = null, val unit: String? = null)