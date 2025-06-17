package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Visibility(val distance: Int? = null, val unit: String? = null)