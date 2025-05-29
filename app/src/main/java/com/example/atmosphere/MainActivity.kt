package com.example.atmosphere

import android.content.pm.PackageManager.Property
import android.opengl.GLU
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.atmosphere.ui.theme.AtmosphereTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Properties
import kotlin.math.ceil
import kotlin.math.round

data class BottomNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)
// Data class to represent a single hourly forecast item
data class HourlyForecastItem(
    val timestamp: String, // Unix timestamp for the hour
    val weatherIconUrl: String,
    val temperature: Int
) {

}
data class WeatherDay(
    val date: String,
    val weatherIconResId: Int, // Resource ID for the weather icon (e.g., R.drawable.ic_sunny)
    val lowTemp: Int,
    val highTemp: Int
)

@Composable
fun WeeklyForecastCard(weatherDay: ForecastDay) {
    val weatherIconUrl = weatherDay.daytimeForecast.weatherCondition.iconBaseUri + ".png"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )  {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Date on the left
            Text(
                text = weatherDay.displayDate.day.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.7f) // Give more weight to the date
            )

            Spacer(modifier = Modifier.width(8.dp))

            Log.d("IconDebug", "Loading icon from URL : $weatherIconUrl")
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
                contentDescription = "Weather icon",
            )
            Log.d(imageLoader.toString(), "imageLoader")

            Spacer(modifier = Modifier.width(16.dp))

            // Low Temperature
            Text(
                text = "${round(weatherDay.minTemperature.degrees).toInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Progress Bar (representing temperature range)
            val temperatureRange = weatherDay.maxTemperature.degrees - weatherDay.minTemperature.degrees
            val progress = if (temperatureRange > 0) {
                (weatherDay.maxTemperature.degrees - weatherDay.minTemperature.degrees).toFloat() / 30f // Assuming a max range for scale, adjust as needed
            } else {
                0f
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .weight(1f) // Fills available space
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )

            Spacer(modifier = Modifier.width(8.dp))

            // High Temperature
            Text(
                text = "${round(weatherDay.maxTemperature.degrees).toInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeeklyForecastCardTemplate(weatherDay: WeatherDay) {
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Date on the left
            Text(
                text = "Monday",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.7f) // Give more weight to the date
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(R.drawable.sunny),
                contentDescription = "Sunny",
                modifier = Modifier
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Low Temperature
            Text(
                text = "${weatherDay.lowTemp}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Progress Bar (representing temperature range)
            val temperatureRange = weatherDay.highTemp - weatherDay.lowTemp
            val progress = if (temperatureRange > 0) {
                (weatherDay.highTemp - weatherDay.lowTemp).toFloat() / 30f // Assuming a max range for scale, adjust as needed
            } else {
                0f
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .weight(1f) // Fills available space
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )

            Spacer(modifier = Modifier.width(8.dp))

            // High Temperature
            Text(
                text = "${weatherDay.highTemp}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Composable
fun WeeklyForecastListTemplate(weeklyForecast: List<WeatherDay>)
{
    // Generate dummy data for 7 days
    val weatherData = remember {
        val today = LocalDate.now()
        List(7) { i ->
            val date = today.plusDays(i.toLong())
            val formatter = DateTimeFormatter.ofPattern("EEE, MMM d") // e.g., Mon, May 24
            WeatherDay(
                date = date.format(formatter),
                weatherIconResId = when (i) {
                    0 -> R.drawable.sunny // Replace with your actual drawables
                    1 -> R.drawable.sunny
                    2 -> R.drawable.sunny
                    3 -> R.drawable.sunny
                    4 -> R.drawable.sunny
                    5 -> R.drawable.sunny
                    else -> R.drawable.sunny
                },
                lowTemp = 15 + i, // Example low temps
                highTemp = 60 + i * 2 // Example high temps
            )
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weatherData) { weatherDay ->
            WeeklyForecastCardTemplate(weatherDay = weatherDay)
        }
    }
}

@Composable
fun WeeklyForecastList(weeklyForecast: DailyWeather)
{
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weeklyForecast.forecastDays) { weatherDay ->
            WeeklyForecastCard(weatherDay = weatherDay)
        }
    }
}


fun convertTo12HourFormat(hourString: String): String {
    return try {
        // Assume the string is just the hour, so append ":00" to match "HH:mm"
        val time24HourWithMinutes = "$hourString:00"
        val defaultLocale = java.util.Locale.getDefault()
        val inputFormat = SimpleDateFormat("HH", defaultLocale)
        val outputFormat = SimpleDateFormat("h a", defaultLocale) // Or "h a" if you don't want ":00"

        val date = inputFormat.parse(time24HourWithMinutes)

        if (date != null) {
            outputFormat.format(date)
        } else {
            hourString // Fallback
        }
    } catch (e: Exception) {
        // Log.e("TimeFormat", "Error converting hour string: $hourString", e)
        hourString // Fallback
    }
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AtmosphereTheme {
                val weatherViewModel: WeatherViewModel = viewModel() // Obtain ViewModel here

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Cinnaminson, NJ")
                            },
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
    fun HomePage(contentPadding: PaddingValues, viewModel: WeatherViewModel) { // contentPadding is passed from NavHost
        val sampleHourlyForecasts = remember {
            listOf(
                HourlyForecastItem("11", "https://maps.gstatic.com/weather/v1/sunny.png", 70),
                HourlyForecastItem("12", "https://maps.gstatic.com/weather/v1/sunny.png", 72),
                HourlyForecastItem("13", "https://maps.gstatic.com/weather/v1/sunny.png", 75),
                HourlyForecastItem("14", "https://maps.gstatic.com/weather/v1/sunny.png", 68),
                HourlyForecastItem("15", "https://maps.gstatic.com/weather/v1/sunny.png", 65),
                HourlyForecastItem("16", "https://maps.gstatic.com/weather/v1/sunny.png", 66),
                HourlyForecastItem("17", "https://maps.gstatic.com/weather/v1/sunny.png", 62),
            )
        }


        val currentWeatherData by viewModel.currentWeatherData.collectAsState()
        val errorMessage by viewModel.currentErrorMessage.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()

        val weeklyWeatherData by viewModel.weeklyWeatherData.collectAsState()
        val hourlyWeatherData by viewModel.hourlyWeatherData.collectAsState()

        LaunchedEffect(Unit) { // Or trigger from a button click
            if (currentWeatherData == null) {
                viewModel.fetchCurrentWeather(39.996, -74.992, "")
            }
        }

        LaunchedEffect(Unit) { // Or trigger from a button click
            if (weeklyWeatherData == null) {
                viewModel.fetchWeeklyWeather(39.996, -74.992, "")
            }
        }

        LaunchedEffect(Unit) { // Or trigger from a button click
            if (hourlyWeatherData == null) {
                viewModel.fetchHourlytWeather(39.996, -74.992, "")
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            if (currentWeatherData != null && weeklyWeatherData != null && hourlyWeatherData != null)
            {
                CurrentConditionsView(
                    System.currentTimeMillis(),
                    currentWeatherData!!.temperature.degrees.toInt(),
                    currentWeatherData!!.weatherCondition.iconBaseUri + ".png",
                    weeklyWeatherData!!.forecastDays.first().maxTemperature.degrees.toInt(),
                    weeklyWeatherData!!.forecastDays.first().minTemperature.degrees.toInt(),
                    currentWeatherData!!.weatherCondition.description.text,
                    currentWeatherData!!.feelsLikeTemperature.degrees.toInt())
            }
            else {
                CurrentConditionsView()
            }

            //Spacer(modifier = Modifier.height(16.dp))
            if (hourlyWeatherData != null)
            {
                HourlyForecastCard(Modifier, hourlyWeatherData!!.forecastHours)
            }
            else {
                //HourlyForecastCard(hourlyForecasts = sampleHourlyForecasts)
            }
            //Spacer(modifier = Modifier.height(16.dp))
            if (weeklyWeatherData != null)
            {
                WeeklyForecastList(weeklyWeatherData!!)
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
            // Optional: Add a Surface for elevation or background
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp), // Optional: Rounded corners
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Distribute space
                verticalAlignment = Alignment.Top // Align content to the top of the row
            ) {
                // --- Left Column ---
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items in this column
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
                        Log.d("IconDebug", "Loading icon from URL : $weatherIconUrl")
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
                            contentDescription = "Weather icon",
                        )
                        Log.d(imageLoader.toString(), "imageLoader")
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
// --- Right Column ---
                Column(
                    horizontalAlignment = Alignment.End, // Align text to the end (right)
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = weatherDescription,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End // Ensure text aligns to the end
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
            // Optionally show a message or hide the card if there's no data
            Text("Hourly forecast data is not available.", modifier = modifier.padding(16.dp))
            return
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Padding around the card
            shape = RoundedCornerShape(12.dp),
            ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()), // Enable horizontal scrolling
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between each hourly item
                ) {
                    hourlyForecasts.forEach { forecastItem ->
                        HourlyForecastItemView(item = forecastItem)
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
        val weatherIconUrl = item.weatherCondition.iconBaseUri + ".png"
        Column(
            modifier = modifier.padding(vertical = 4.dp), // Small vertical padding for each item
            verticalArrangement = Arrangement.spacedBy(8.dp), // Space between elements in the column
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = convertTo12HourFormat(item.displayDateTime.hours.toString()),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(weatherIconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather icon for ${convertTo12HourFormat(item.displayDateTime.toString())}" , // Be descriptive
                modifier = Modifier.size(24.dp) // Adjust icon size as needed
            )
            Text(
                text = "${round(item.temperature.degrees).toInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // templates
    @Composable
    fun HourlyForecastCardTemplate(
        modifier: Modifier = Modifier,
        hourlyForecasts: List<HourlyForecastItem>
    ) {
        if (hourlyForecasts.isEmpty()) {
            // Optionally show a message or hide the card if there's no data
            Text("Hourly forecast data is not available.", modifier = modifier.padding(16.dp))
            return
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Padding around the card
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()), // Enable horizontal scrolling
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between each hourly item
                ) {
                    hourlyForecasts.forEach { forecastItem ->
                        HourlyForecastItemViewTemplate(item = forecastItem)
                    }
                }
            }
        }
    }

    @Composable
    fun HourlyForecastItemViewTemplate(
        modifier: Modifier = Modifier,
        item: HourlyForecastItem
    ) {
        Column(
            modifier = modifier.padding(vertical = 4.dp), // Small vertical padding for each item
            verticalArrangement = Arrangement.spacedBy(8.dp), // Space between elements in the column
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = convertTo12HourFormat(item.timestamp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Image(
                painter = painterResource(id = R.drawable.sunny),
                contentDescription = "Weather icon for ${convertTo12HourFormat(item.timestamp)}",
                modifier = Modifier.size(24.dp) // Adjust icon size as needed,

            )
            Text(
                text = "${item.temperature}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }



    @Preview(showBackground = true, widthDp = 380)
    @Composable
    fun HomePagePreview()
    {
        val sampleHourlyForecasts = remember {
            listOf(
                HourlyForecastItem("11", "ttps://maps.gstatic.com/weather/v1/sunny.png", 70),
                HourlyForecastItem("12", "ttps://maps.gstatic.com/weather/v1/sunny.pngg", 72),
                HourlyForecastItem("13", "https://maps.gstatic.com/weather/v1/sunny.png", 75),
                HourlyForecastItem("14", "https://maps.gstatic.com/weather/v1/sunny.png", 68),
                HourlyForecastItem("15", "ttps://maps.gstatic.com/weather/v1/sunny.png", 65),
                HourlyForecastItem("16", "ttps://maps.gstatic.com/weather/v1/sunny.png", 66),
                HourlyForecastItem("17", "ttps://maps.gstatic.com/weather/v1/sunny.png", 62),
            )
        }
        val weatherData = remember {
            val today = LocalDate.now()
            List(7) { i ->
                val date = today.plusDays(i.toLong())
                WeatherDay(
                    date = "Monday",
                    weatherIconResId = when (i) {
                        0 -> R.drawable.sunny // Replace with your actual drawables
                        1 -> R.drawable.sunny
                        2 -> R.drawable.sunny
                        3 -> R.drawable.sunny
                        4 -> R.drawable.sunny
                        5 -> R.drawable.sunny
                        else -> R.drawable.sunny
                    },
                    lowTemp = 15 + i, // Example low temps
                    highTemp = 60 + i * 2 // Example high temps
                )
            }
        }
        AtmosphereTheme {
            Column(

            ) {
                CurrentConditionsView()
                HourlyForecastCardTemplate(Modifier, hourlyForecasts = sampleHourlyForecasts)
                WeeklyForecastListTemplate(weeklyForecast = weatherData)
            }

        }
    }

    @Preview(showBackground = true, widthDp = 380)
    @Composable
    fun CurrentConditionsPreview() { // Renamed for clarity, was GreetingPreview
        AtmosphereTheme {
            MainActivity().CurrentConditionsView() // This might not be ideal if it relies on MainActivity state not available in preview
        }
    }

    @Preview(showBackground = true, widthDp = 380)
    @Composable
    fun HourlyForecastCardPreview() {
        val sampleHourlyForecasts = remember {
            listOf(
                HourlyForecastItem("11", "ttps://maps.gstatic.com/weather/v1/sunny.png", 70),
                HourlyForecastItem("12", "ttps://maps.gstatic.com/weather/v1/sunny.pngg", 72),
                HourlyForecastItem("13", "https://maps.gstatic.com/weather/v1/sunny.png", 75),
                HourlyForecastItem("14", "https://maps.gstatic.com/weather/v1/sunny.png", 68),
                HourlyForecastItem("15", "ttps://maps.gstatic.com/weather/v1/sunny.png", 65),
                HourlyForecastItem("16", "ttps://maps.gstatic.com/weather/v1/sunny.png", 66),
                HourlyForecastItem("17", "ttps://maps.gstatic.com/weather/v1/sunny.png", 62),
            )
        }
        AtmosphereTheme {
            HourlyForecastCardTemplate(hourlyForecasts = sampleHourlyForecasts)
        }
    }

    @Preview(showBackground = true, widthDp = 380)
    @Composable
    fun WeeklyForecastCardPreview() {
        val weatherData = remember {
            val today = LocalDate.now()
            List(7) { i ->
                val date = today.plusDays(i.toLong())
                WeatherDay(
                    date = "Monday",
                    weatherIconResId = when (i) {
                        0 -> R.drawable.sunny // Replace with your actual drawables
                        1 -> R.drawable.sunny
                        2 -> R.drawable.sunny
                        3 -> R.drawable.sunny
                        4 -> R.drawable.sunny
                        5 -> R.drawable.sunny
                        else -> R.drawable.sunny
                    },
                    lowTemp = 15 + i, // Example low temps
                    highTemp = 60 + i * 2 // Example high temps
                )
            }
        }
        AtmosphereTheme {
            WeeklyForecastListTemplate(weatherData)
        }
    }
    }








