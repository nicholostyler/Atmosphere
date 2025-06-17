package com.example.atmosphere

import android.content.Context
import com.example.atmosphere.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class WeatherRepository(context: Context) {
    private val dao = WeatherDatabase.getDatabase(context).weatherCacheDao()
    private val json = Json { ignoreUnknownKeys = true }

    private val api = RetrofitClient().weatherApiService

    private val cacheExpiryMillis = 30 * 60 * 1000 // 30 minutes

    suspend fun getCurrentWeather(
        apiKey: String,
        lat: Double,
        lon: Double
    ): CachedResult<CurrentWeather> {
        return withContext(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val cached = dao.getWeather("current")
            if (cached != null && now - cached.timestamp < cacheExpiryMillis) {
                val parsed = runCatching {
                    json.decodeFromString(CurrentWeather.serializer(), cached.jsonData)
                }.getOrNull()
                return@withContext CachedResult(parsed, cached.timestamp)
            }

            val response = api.getCurrentConditions(apiKey, lat, lon, "IMPERIAL")
            if (response.isSuccessful) {
                response.body()?.let {
                    val jsonData = json.encodeToString(CurrentWeather.serializer(), it)
                    dao.insertWeather(WeatherCacheEntity("current", jsonData, now))
                    return@withContext CachedResult(it, now)
                }
            }
            CachedResult(null, now)
        }
    }

    suspend fun getHourlyWeather(apiKey: String, lat: Double, lon: Double): CachedResult<HourlyWeather> {
        return withContext(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val cached = dao.getWeather("hourly")
            if (cached != null && now - cached.timestamp < cacheExpiryMillis) {
                val parsed = runCatching {
                    json.decodeFromString(HourlyWeather.serializer(), cached.jsonData)
                }.getOrNull()
                return@withContext CachedResult(parsed, cached.timestamp)
            }

            val response = api.getHourlyForecast(apiKey, lat, lon, 24, "IMPERIAL")
            if (response.isSuccessful) {
                response.body()?.let {
                    val jsonData = json.encodeToString(HourlyWeather.serializer(), it)
                    dao.insertWeather(WeatherCacheEntity("hourly", jsonData, now))
                    return@withContext CachedResult(it, now)
                }
            }
            CachedResult(null, now)
        }
    }

    suspend fun getWeeklyWeather(apiKey: String, lat: Double, lon: Double): CachedResult<DailyWeather> {
        return withContext(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val cached = dao.getWeather("weekly")
            if (cached != null && now - cached.timestamp < cacheExpiryMillis) {
                val parsed = runCatching {
                    json.decodeFromString(DailyWeather.serializer(), cached.jsonData)
                }.getOrNull()
                return@withContext CachedResult(parsed, cached.timestamp)
            }

            val response = api.getWeeklyForecast(apiKey, lat, lon, 7, 7, "IMPERIAL")
            if (response.isSuccessful) {
                response.body()?.let {
                    val jsonData = json.encodeToString(DailyWeather.serializer(), it)
                    dao.insertWeather(WeatherCacheEntity("weekly", jsonData, now))
                    return@withContext CachedResult(it,now)
                }
            }
            CachedResult(null, now)
        }
    }
}

