package com.example.atmosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.atmosphere.ui.theme.AtmosphereTheme
import kotlinx.datetime.format.Padding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import kotlin.math.round

data class BottomNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)

data class HourlyForecastItem(
    val timestamp: String,
    val weatherIconUrl: String,
    val temperature: Int
)

data class WeatherDay(
    val date: String,
    val weatherIconResId: Int,
    val lowTemp: Int,
    val highTemp: Int
)

fun getDayOfWeekFromString(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("M-d-yyyy")
        val date = LocalDate.parse(dateString, formatter)
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, java.util.Locale.getDefault())
    } catch (e: Exception) {
        "Day"
    }
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtmosphereTheme {
                val context = LocalContext.current
                val contextFactory = remember {WeatherViewModelFactory(context)}
                val weatherViewModel: WeatherViewModel = viewModel(factory = contextFactory)

                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        // Trigger refresh when the app is resumed
                        weatherViewModel.fetchCurrentWeather()
                        weatherViewModel.fetchWeeklyWeather()
                        weatherViewModel.fetchHourlyWeather()
                    }
                }
                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            title = { Text("Cinnaminson, NJ") },
                            colors = TopAppBarDefaults.topAppBarColors()
                        )
                    },
                ) { contentPadding ->
                    HomePage(contentPadding, weatherViewModel)
                }
            }
        }
    }

    @Composable
    fun HomePage(contentPadding: PaddingValues, viewModel: WeatherViewModel) {
        val currentWeatherData by viewModel.currentWeatherData.collectAsState()
        val errorMessage by viewModel.currentErrorMessage.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val weeklyWeatherData by viewModel.weeklyWeatherData.collectAsState()
        val hourlyWeatherData by viewModel.hourlyWeatherData.collectAsState()
        val timestamp by viewModel.currentWeatherTimestamp.collectAsState()

        LaunchedEffect(Unit) {
            if (currentWeatherData == null) {
                viewModel.fetchCurrentWeather()
            }
            if (weeklyWeatherData == null) {
                viewModel.fetchWeeklyWeather()
            }
            if (hourlyWeatherData == null) {
                viewModel.fetchHourlyWeather()
            }
        }

        if (hourlyWeatherData == null && currentWeatherData == null && weeklyWeatherData == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(top = contentPadding.calculateTopPadding()))
                    .verticalScroll(rememberScrollState()),
            ) {
                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                if (currentWeatherData != null && weeklyWeatherData != null && hourlyWeatherData != null) {
                    CurrentConditionsView(
                        timestamp ?: System.currentTimeMillis(),
                        currentWeatherData?.temperature?.degrees?.toInt() ?: 0,
                        currentWeatherData?.weatherCondition?.iconBaseUri?.plus(".png")
                            ?: "https://maps.gstatic.com/weather/v1/cloudy.png",
                        weeklyWeatherData?.forecastDays?.firstOrNull()?.maxTemperature?.degrees?.toInt()
                            ?: 0,
                        weeklyWeatherData?.forecastDays?.firstOrNull()?.minTemperature?.degrees?.toInt()
                            ?: 0,
                        currentWeatherData?.weatherCondition?.description?.text ?: "Unknown",
                        currentWeatherData?.feelsLikeTemperature?.degrees?.toInt() ?: 0
                    )
                } else {
                    // Placeholder or loading
                    CurrentConditionsView()
                }

                if (!hourlyWeatherData?.forecastHours.isNullOrEmpty()) {
                    HourlyForecastCard(Modifier, hourlyWeatherData?.forecastHours ?: emptyList())
                } else {
                    Text("No hourly forecast data.", modifier = Modifier.padding(16.dp))
                }

                if (!weeklyWeatherData?.forecastDays.isNullOrEmpty()) {
                    weeklyWeatherData?.let { WeeklyForecastList(it) }
                } else {
                    Text("No weekly forecast data.", modifier = Modifier.padding(16.dp))
                }

                currentWeatherData?.let { WeatherCardsList(it) }
            }
        }
    }

    @Composable
    fun CurrentConditionsView(
        timestamp: Long = System.currentTimeMillis(),
        currentTemperature: Int = 72,
        weatherIconUrl: String = "https://maps.gstatic.com/weather/v1/flurries.png",
        highTemperature: Int = 75,
        lowTemperature: Int = 68,
        weatherDescription: String = "Partly Cloudy",
        feelsLikeTemperature: Int = 74
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left Column
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Updated: ${SimpleDateFormat("h:mm a").format(timestamp)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$currentTemperature°",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val imageLoader = LocalContext.current.imageLoader.newBuilder()
                            .logger(DebugLogger())
                            .build()
                        AsyncImage(
                            imageLoader = imageLoader,
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(weatherIconUrl)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.cloudy),
                            error = painterResource(R.drawable.cloudy),
                            contentDescription = "Weather icon"
                        )
                    }
                    Row {
                        Text(
                            text = "H: $highTemperature°",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "L: $lowTemperature°",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                // Right Column
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = weatherDescription,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "Feels like $feelsLikeTemperature°",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }

    @Composable
    fun HourlyForecastCard(
        modifier: Modifier = Modifier,
        hourlyForecasts: List<ForecastHour>
    ) {
        if (hourlyForecasts.isEmpty()) {
            Text("Hourly forecast data is not available.", modifier = modifier.padding(16.dp))
            return
        }
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    hourlyForecasts.forEach { forecastItem ->
                        forecastItem?.let {
                            HourlyForecastItemView(item = it)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun HourlyForecastItemView(
        modifier: Modifier = Modifier,
        item: ForecastHour
    ) {
        val weatherIconUrl = item.weatherCondition?.iconBaseUri + ".png"
        Column(
            modifier = modifier.padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = convertTo12HourFormat(item.displayDateTime?.hours.toString()),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(weatherIconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather icon for ${convertTo12HourFormat(item.displayDateTime.toString())}",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "${item.temperature?.degrees?.let { round(it).toInt() }}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun WeeklyForecastList(weeklyForecast: DailyWeather) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            weeklyForecast.forecastDays?.forEach { weatherDay ->
                WeeklyForecastCard(weatherDay = weatherDay)
            }
        }

    }

    @Composable
    fun WeeklyForecastCard(weatherDay: ForecastDay) {
        val weatherIconUrl = weatherDay.daytimeForecast?.weatherCondition?.iconBaseUri + ".png"
        val day = getDayOfWeekFromString("${weatherDay.displayDate?.month}-${weatherDay.displayDate?.day}-${weatherDay.displayDate?.year}")
        val lowTemp = weatherDay.minTemperature?.degrees?.toDouble()?.let { round(it).toInt() } ?: return
        val highTemp = weatherDay.maxTemperature?.degrees?.toDouble()?.let { round(it).toInt() } ?: return
        val range = (highTemp - lowTemp).coerceAtLeast(1)
        val normalized = range / 30f

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Day label
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💧 ${weatherDay.daytimeForecast?.precipitation?.probability?.percent ?: 0}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF81A4FF)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Weather icon
                AsyncImage(
                    imageLoader = LocalContext.current.imageLoader.newBuilder().logger(DebugLogger()).build(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(weatherIconUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.cloudy),
                    error = painterResource(R.drawable.cloudy),
                    contentDescription = "Weather icon",
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Min temp
                Text(
                    text = "$lowTemp°",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Temperature bar (fills remaining horizontal space)
                TemperatureRangeBar(
                    rangeFraction = normalized,
                    modifier = Modifier
                        .weight(2f)
                        .height(8.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Max temp
                Text(
                    text = "$highTemp°",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
    }

    @Composable
    fun TemperatureRangeBar(
        rangeFraction: Float,
        modifier: Modifier = Modifier,
        barColor: Color = MaterialTheme.colorScheme.primary
    ) {
        val clampedFraction = rangeFraction.coerceIn(0f, 1f)

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Transparent),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(clampedFraction)
                    .background(barColor, RoundedCornerShape(4.dp))
            )
        }
    }



    // Utility
    fun convertTo12HourFormat(hourString: String): String {
        return try {
            val time24HourWithMinutes = "$hourString:00"
            val defaultLocale = java.util.Locale.getDefault()
            val inputFormat = SimpleDateFormat("HH", defaultLocale)
            val outputFormat = SimpleDateFormat("h a", defaultLocale)
            val date = inputFormat.parse(time24HourWithMinutes)
            if (date != null) outputFormat.format(date) else hourString
        } catch (e: Exception) {
            hourString
        }
    }

}
