package com.example.atmosphere
import CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("v1/currentConditions:lookup")
    suspend fun getCurrentConditions(
        @Query("key") apiKey: String,
        @Query("location.latitude") latitude: Double,
        @Query("location.longitude") longitude: Double,
        @Query("units_system") unitssystem: String
    ): Response<CurrentWeather>

    @GET("v1/forecast/days:lookup")
    suspend fun getWeeklyForecast(
        @Query("key") apiKey: String,
        @Query("location.latitude") latitude: Double,
        @Query("location.longitude") longitude: Double,
        @Query("days") days: Int,
        @Query("pageSize") pageSize: Int,
        @Query("units_system") unitssystem: String
    ): Response<DailyWeather>

    @GET("v1/forecast/hours:lookup")
    suspend fun getHourlyForecast(
        @Query("key") apiKey: String,
        @Query("location.latitude") latitude: Double,
        @Query("location.longitude") longitude: Double,
        @Query("hours") hours: Int,
        @Query("units_system") unitssystem: String
    ): Response<HourlyWeather>

}
