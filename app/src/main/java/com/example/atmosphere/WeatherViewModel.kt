package com.example.atmosphere

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class WeatherViewModel(private val context: Context) : ViewModel() {

    private val repository = WeatherRepository(context)
    private val freshnessThreshold = 30 * 60 * 1000 // 30 minutes

    private val _currentWeatherData = MutableStateFlow<CurrentWeather?>(null)
    val currentWeatherData: StateFlow<CurrentWeather?> = _currentWeatherData

    private val _weeklyWeatherData = MutableStateFlow<DailyWeather?>(null)
    val weeklyWeatherData: StateFlow<DailyWeather?> = _weeklyWeatherData

    private val _hourlyWeatherData = MutableStateFlow<HourlyWeather?>(null)
    val hourlyWeatherData: StateFlow<HourlyWeather?> = _hourlyWeatherData

    private val _currentErrorMessage = MutableStateFlow<String?>(null)
    val currentErrorMessage: StateFlow<String?> = _currentErrorMessage

    private val _currentWeatherTimestamp = MutableStateFlow<Long?>(null)
    val currentWeatherTimestamp: StateFlow<Long?> = _currentWeatherTimestamp

    private val _hourlyWeatherTimestamp = MutableStateFlow<Long?>(null)
    val hourlyWeatherTimestamp: StateFlow<Long?> = _currentWeatherTimestamp

    private val _weelyWeatherTimestamp = MutableStateFlow<Long?>(null)
    val weeklyWeatherTimestamp: StateFlow<Long?> = _currentWeatherTimestamp

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val apiKey = ""
    private val lat = 39.997
    private val long = -74.992

    init {
        Log.d("WeatherViewModel", "ViewModel INIT. HashCode: ${this.hashCode()}")
    }

    fun fetchCurrentWeather() {
        val job = viewModelScope.launch {
            if ( _currentWeatherData.value != null &&
                _currentWeatherTimestamp.value != null &&
                (System.currentTimeMillis() - _currentWeatherTimestamp.value!!) < freshnessThreshold
                ) return@launch
            _isLoading.value = true
            _currentErrorMessage.value = null
            _currentWeatherData.value = null // Clear previous data while loading


            try {
                val result = repository.getCurrentWeather(apiKey, lat, long)
                _currentWeatherData.value = result.data
                _currentWeatherTimestamp.value = result.timestamp
                if (result.data == null) _currentErrorMessage.value = "Error: Empty response from server."
            } catch (e: Exception) {
                _currentErrorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchHourlyWeather() {
        viewModelScope.launch {
            if ( _currentWeatherData.value != null &&
                _hourlyWeatherTimestamp.value != null &&
                (System.currentTimeMillis() - _hourlyWeatherTimestamp.value!!) < freshnessThreshold
            ) return@launch

            _isLoading.value = true
            _currentErrorMessage.value = null
            _currentWeatherData.value = null

            try {
                val result = repository.getHourlyWeather(apiKey, lat, long)
                _hourlyWeatherData.value = result.data
                if (result.data == null) _currentErrorMessage.value = "No hourly weather data."
            } catch (e: Exception) {
                _currentErrorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchWeeklyWeather() {
        viewModelScope.launch {
            if ( _currentWeatherData.value != null &&
                _hourlyWeatherTimestamp.value != null &&
                (System.currentTimeMillis() - _hourlyWeatherTimestamp.value!!) < freshnessThreshold
            ) return@launch

            _isLoading.value = true
            _currentErrorMessage.value = null
            try {
                val result = repository.getWeeklyWeather(apiKey, lat, long)
                _weeklyWeatherData.value = result.data
                if (result.data == null) _currentErrorMessage.value = "No weekly weather data."
            } catch (e: Exception) {
                _currentErrorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }




    override fun onCleared() {
        super.onCleared()
        Log.w("WeatherViewModel", "ON_CLEARED. ViewModel HashCode: ${this.hashCode()}. Coroutines in viewModelScope are being cancelled.")
    }


}

