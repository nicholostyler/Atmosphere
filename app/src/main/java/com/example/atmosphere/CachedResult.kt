package com.example.atmosphere

data class CachedResult<T> (
    val data: T?,
    val timestamp: Long
)