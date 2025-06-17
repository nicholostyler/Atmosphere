package com.example.atmosphere

import kotlinx.serialization.Serializable

@Serializable
data class Description(val text: String? = null, val languageCode: String? = null)