package com.example.atmosphere

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val key: String,       // "current", "hourly", or "weekly"
    val jsonData: String,              // Serialized JSON string
    val timestamp: Long                // For freshness check
)


@Dao
interface WeatherCacheDao {
    @Query("SELECT * FROM weather_cache WHERE `key` = :key")
    suspend fun getWeather(key: String): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(entity: WeatherCacheEntity)
}
