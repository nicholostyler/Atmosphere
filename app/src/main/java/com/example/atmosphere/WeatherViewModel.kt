package com.example.atmosphere

import CurrentWeather
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class WeatherViewModel : ViewModel() {

    private val _currentWeatherData = MutableStateFlow<CurrentWeather?>(null)
    val currentWeatherData: StateFlow<CurrentWeather?> = _currentWeatherData

    private val _weeklyWeatherData = MutableStateFlow<DailyWeather?>(null)
    val weeklyWeatherData: StateFlow<DailyWeather?> = _weeklyWeatherData

    private val _hourlyWeatherData = MutableStateFlow<HourlyWeather?>(null)
    val hourlyWeatherData: StateFlow<HourlyWeather?> = _hourlyWeatherData

    private val _currentErrorMessage = MutableStateFlow<String?>(null)
    val currentErrorMessage: StateFlow<String?> = _currentErrorMessage

    // You might want a loading state
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        Log.d("WeatherViewModel", "ViewModel INIT. HashCode: ${this.hashCode()}")
    }

    fun fetchCurrentWeather(latitude: Double, longitude: Double, apiKey: String) {
        Log.i("WeatherViewModel", "FETCH_CURRENT_WEATHER_CALLED. ViewModel HashCode: ${this.hashCode()}")

        // Log the state of the viewModelScope's job BEFORE launching
        Log.d("WeatherViewModel", "viewModelScope.isActive: ${viewModelScope.coroutineContext.job.isActive}")
        Log.d("WeatherViewModel", "viewModelScope.isCancelled: ${viewModelScope.coroutineContext.job.isCancelled}")

        val job = viewModelScope.launch {
            // THIS IS THE VERY FIRST LINE INSIDE THE COROUTINE
            Log.i("WeatherViewModel", "COROUTINE_STARTED. ViewModel HashCode: ${this.hashCode()}")
            _isLoading.value = true
            _currentErrorMessage.value = null
            _currentWeatherData.value = null // Clear previous data while loading

            try {
                Log.d("WeatherViewModel", "TRY_BLOCK_ENTERED. Calling API...")
                // Replace with your actual RetrofitClient setup if it's different
                val response = RetrofitClient().weatherApiService.getCurrentConditions(
                    apiKey = apiKey, // Make sure param names match your service definition
                    latitude = latitude,
                    longitude = longitude,
                    unitssystem = "IMPERIAL"
                )

                Log.d("WeatherViewModel", "API_RESPONSE_RECEIVED. Code: ${response.code()}, Successful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        Log.i("WeatherViewModel", "API_SUCCESS. Data: $weatherData")
                        _currentWeatherData.value = weatherData
                    } else {
                        Log.e("WeatherViewModel", "API_SUCCESS_BUT_NULL_BODY. Code: ${response.code()}")
                        _currentErrorMessage.value = "Error: Empty response from server."
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("WeatherViewModel", "API_ERROR. Code: ${response.code()}, Message: ${response.message()}, Body: $errorBody")
                    _currentErrorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: CancellationException) {
                Log.w("WeatherViewModel", "CATCH_BLOCK_CANCELLATION_EXCEPTION. ViewModel HashCode: ${this.hashCode()}", e)
                _currentErrorMessage.value = "Request was cancelled."
                throw e // Re-throw cancellation exceptions as per best practice
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "CATCH_BLOCK_EXCEPTION. ViewModel HashCode: ${this.hashCode()}", e)
                _currentErrorMessage.value = "Network error: ${e.message}"
            } finally {
                Log.i("WeatherViewModel", "FINALLY_BLOCK_EXECUTED. ViewModel HashCode: ${this.hashCode()}")
                _isLoading.value = false
            }
        }
        // Log job status immediately after launch
        Log.d("WeatherViewModel", "Job isActive: ${job.isActive}, isCancelled: ${job.isCancelled}, isCompleted: ${job.isCompleted}")
        Log.d("WeatherViewModel", "FETCH_CURRENT_WEATHER_ENDED_SYNC_PART. ViewModel HashCode: ${this.hashCode()}")
    }

    fun fetchHourlytWeather(latitude: Double, longitude: Double, apiKey: String) {
        Log.i("WeatherViewModel", "FETCH_Hourly_WEATHER_CALLED. ViewModel HashCode: ${this.hashCode()}")

        // Log the state of the viewModelScope's job BEFORE launching
        Log.d("WeatherViewModel", "viewModelScope.isActive: ${viewModelScope.coroutineContext.job.isActive}")
        Log.d("WeatherViewModel", "viewModelScope.isCancelled: ${viewModelScope.coroutineContext.job.isCancelled}")

        val job = viewModelScope.launch {
            // THIS IS THE VERY FIRST LINE INSIDE THE COROUTINE
            Log.i("WeatherViewModel", "COROUTINE_STARTED. ViewModel HashCode: ${this.hashCode()}")
            _isLoading.value = true
            _currentErrorMessage.value = null
            _hourlyWeatherData.value = null // Clear previous data while loading

            try {
                Log.d("WeatherViewModel", "TRY_BLOCK_ENTERED. Calling API...")
                // Replace with your actual RetrofitClient setup if it's different
                val response = RetrofitClient().weatherApiService.getHourlyForecast(
                    apiKey = apiKey, // Make sure param names match your service definition
                    latitude = latitude,
                    longitude = longitude,
                    hours = 24,
                    unitssystem = "IMPERIAL"
                )

                Log.d("WeatherViewModel", "API_RESPONSE_RECEIVED. Code: ${response.code()}, Successful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        Log.i("WeatherViewModel", "API_SUCCESS. Data: $weatherData")
                        _hourlyWeatherData.value = weatherData
                    } else {
                        Log.e("WeatherViewModel", "API_SUCCESS_BUT_NULL_BODY. Code: ${response.code()}")
                        _currentErrorMessage.value = "Error: Empty response from server."
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("WeatherViewModel", "API_ERROR. Code: ${response.code()}, Message: ${response.message()}, Body: $errorBody")
                    _currentErrorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: CancellationException) {
                Log.w("WeatherViewModel", "CATCH_BLOCK_CANCELLATION_EXCEPTION. ViewModel HashCode: ${this.hashCode()}", e)
                _currentErrorMessage.value = "Request was cancelled."
                throw e // Re-throw cancellation exceptions as per best practice
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "CATCH_BLOCK_EXCEPTION. ViewModel HashCode: ${this.hashCode()}", e)
                _currentErrorMessage.value = "Network error: ${e.message}"
            } finally {
                Log.i("WeatherViewModel", "FINALLY_BLOCK_EXECUTED. ViewModel HashCode: ${this.hashCode()}")
                _isLoading.value = false
            }
        }
        // Log job status immediately after launch
        Log.d("WeatherViewModel", "Job isActive: ${job.isActive}, isCancelled: ${job.isCancelled}, isCompleted: ${job.isCompleted}")
        Log.d("WeatherViewModel", "FETCH_HOURLY_WEATHER_ENDED_SYNC_PART. ViewModel HashCode: ${this.hashCode()}")
    }

    fun fetchWeeklyWeather(latitude: Double, longitude: Double, apiKey: String) {
        Log.i("WeatherViewModel", "FETCH_WEEKLY_WEATHER_CALLED. ViewModel HashCode: ${this.hashCode()}")

        // Log the state of the viewModelScope's job BEFORE launching
        Log.d("WeatherViewModel", "viewModelScope.isActive: ${viewModelScope.coroutineContext.job.isActive}")
        Log.d("WeatherViewModel", "viewModelScope.isCancelled: ${viewModelScope.coroutineContext.job.isCancelled}")

        if (!viewModelScope.isActive) {
            Log.e("WeatherViewModel", "Attempting to launch on an INACTIVE viewModelScope. Coroutine will NOT run.")
            // Potentially handle this case, maybe re-initialize something if appropriate,
            // or signal an error state.
            _currentErrorMessage.value = "Internal error: ViewModel scope inactive."
            _isLoading.value = false
            return // Don't proceed to launch
        }
        try {
            val job = viewModelScope.launch {
                // THIS IS THE VERY FIRST LINE INSIDE THE COROUTINE
                Log.i(
                    "WeatherViewModel",
                    "COROUTINE_STARTED. ViewModel HashCode: ${this.hashCode()}"
                )
                _isLoading.value = true
                _currentErrorMessage.value = null
                _weeklyWeatherData.value = null

                try {
                    Log.d("WeatherViewModel", "TRY_BLOCK_ENTERED. Calling API...")
                    // Replace with your actual RetrofitClient setup if it's different
                    val response = RetrofitClient().weatherApiService.getWeeklyForecast(
                        apiKey = apiKey, // Make sure param names match your service definition
                        latitude = latitude,
                        longitude = longitude,
                        days = 7,
                        pageSize = 7,
                        unitssystem = "IMPERIAL"
                    )

                    Log.d(
                        "WeatherViewModel",
                        "API_RESPONSE_RECEIVED. Code: ${response.code()}, Successful: ${response.isSuccessful}"
                    )

                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            Log.i("WeatherViewModel", "API_SUCCESS. Data: $weatherData")
                            _weeklyWeatherData.value = weatherData
                        } else {
                            Log.e(
                                "WeatherViewModel",
                                "API_SUCCESS_BUT_NULL_BODY. Code: ${response.code()}"
                            )
                            _currentErrorMessage.value = "Error: Empty response from server."
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Log.e(
                            "WeatherViewModel",
                            "API_ERROR. Code: ${response.code()}, Message: ${response.message()}, Body: $errorBody"
                        )
                        _currentErrorMessage.value =
                            "Error: ${response.code()} - ${response.message()}"
                    }
                } catch (e: CancellationException) {
                    Log.w(
                        "WeatherViewModel",
                        "CATCH_BLOCK_CANCELLATION_EXCEPTION. ViewModel HashCode: ${this.hashCode()}",
                        e
                    )
                    _currentErrorMessage.value = "Request was cancelled."
                    throw e // Re-throw cancellation exceptions as per best practice
                } catch (e: Exception) {
                    Log.e(
                        "WeatherViewModel",
                        "CATCH_BLOCK_EXCEPTION. ViewModel HashCode: ${this.hashCode()}",
                        e
                    )
                    _currentErrorMessage.value = "Network error: ${e.message}"
                } finally {
                    Log.i(
                        "WeatherViewModel",
                        "FINALLY_BLOCK_EXECUTED. ViewModel HashCode: ${this.hashCode()}"
                    )
                    _isLoading.value = false
                }

            }
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Exception BEFORE coroutine launch", e)
        }

}




    override fun onCleared() {
        super.onCleared()
        Log.w("WeatherViewModel", "ON_CLEARED. ViewModel HashCode: ${this.hashCode()}. Coroutines in viewModelScope are being cancelled.")
    }


}

