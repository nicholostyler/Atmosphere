package com.example.atmosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atmosphere.ui.theme.AtmosphereTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sampleForecasts = listOf(
            DailyForecast(date = "Mon, Oct 28", iconId = 1, temperature = "75°F"),
            DailyForecast(date = "Tue, Oct 29", iconId = 2, temperature = "72°F"),
            DailyForecast(date = "Wed, Oct 30", iconId = 3, temperature = "68°F"),
            DailyForecast(date = "Thu, Oct 31", iconId = 1, temperature = "70°F"),
            DailyForecast(date = "Fri, Nov 1", iconId = 2, temperature = "65°F")
        )
        val sampleHourlyForecasts = listOf(
            HourlyForecast("12 PM", 1, "10%"),
            HourlyForecast("1 PM", 2, "20%"),
            HourlyForecast("2 PM", 2, "30%"),
            HourlyForecast("3 PM", 3, "60%"),
            HourlyForecast("4 PM", 3, "80%"),
            HourlyForecast("5 PM", 4, "90%")
        )
        setContent {
            AtmosphereTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                              Text("Cinnaminson, NJ")
                            },
                            colors = TopAppBarDefaults.topAppBarColors()
                        )
                             },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {  // Correct placement of the opening brace

                            CurrentConditionsView()
                            HourlyForecastRow(sampleHourlyForecasts)
                            CurrentConditionsCardRow()
                            WeeklyForecastList(forecasts = sampleForecasts)
                        }
                }
            }
        }

        }
    }

@Composable
fun WeatherIcon(iconId: Int, modifier: Modifier = Modifier) {
    val painter = when (iconId) {
        1 -> painterResource(id = R.drawable.sunny) // Example: Sunny
        2 -> painterResource(id = R.drawable.cloudy ) // Example: Cloudy
        3 -> painterResource(id = R.drawable.rainy)  // Example: Rainy
        4 -> painterResource(id = R.drawable.snowy)  // Example: Snowy
        5 -> painterResource(id = R.drawable.humidity)  // Example: Foggy
        6 -> painterResource(id = R.drawable.windy)  // Example: Windy
        else -> painterResource(id = R.drawable.sunny) // Default to sunny
    }
    Icon(
        painter = painter,
        contentDescription = "Weather Icon", // Important for accessibility
        modifier = modifier.size(48.dp), //  adjust size as needed
        tint = MaterialTheme.colorScheme.onBackground //  use the icon's original color
    )
}

data class HourlyForecast(
    val hour: String,
    val iconId: Int,
    val chanceOfRain: String
)

@Composable
fun HourlyForecastRow(hourlyForecasts: List<HourlyForecast>) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Space between items
    ) {
        // If the list is empty, show a message
        if (hourlyForecasts.isEmpty()) {
            Text(
                text = "No hourly forecast available.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            for (forecast in hourlyForecasts) {
                HourlyForecastItem(forecast = forecast)
            }
        }
    }
}

@Composable
fun HourlyForecastItem(forecast: HourlyForecast) {
    Column(
        modifier = Modifier
            .width(80.dp), // Fixed width for each item
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = forecast.hour,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        WeatherIcon(iconId = forecast.iconId, modifier = Modifier.size(32.dp)) // Smaller icon
        Text(
            text = forecast.chanceOfRain,
            style = MaterialTheme.typography.bodySmall, // Use caption for smaller text
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CurrentConditionsCardItem(modifier: Modifier, category: String, value: String, iconId: Int = 1)
{
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            WeatherIcon(iconId = iconId, modifier = Modifier
                .size(25.dp)
                .align(Alignment.CenterHorizontally)) // Smaller icon
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall, // Use caption for smaller text
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun CurrentConditionsCardRow()
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CurrentConditionsCardItem(Modifier.weight(1f), "Wind", "20mph", 6)
        CurrentConditionsCardItem(Modifier.weight(1f), "Humidity", "50%", 5)
        CurrentConditionsCardItem(Modifier.weight(1f), "Rain", "80%", 3)
        }
    }


@Composable
fun CurrentConditionsView()
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        WeatherIcon(iconId = 1, modifier = Modifier.size(50.dp))
        Text(
            text = "30",
            style = MaterialTheme.typography.displayLarge,
            color = Color.Blue,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Column()
        {
            Text(
                text = "Fair",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,

                )
            Text(
                text = "31 / 24",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Feels like 30",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DailyForecastRow(forecast: DailyForecast, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Weather Icon (Left)
        WeatherIcon(iconId = forecast.iconId, modifier = Modifier.size(40.dp))

        Spacer(modifier = Modifier.width(16.dp)) // Spacing

        // Date (Middle) - Occupies available space and centers text
        Text(
            text = forecast.date,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f), // Takes up remaining space in the middle
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(16.dp)) // Spacing

        // Temperature (Right)
        Text(
            text = forecast.temperature,
            style = MaterialTheme.typography.titleMedium, // Slightly larger for temperature
            textAlign = TextAlign.End // Align text to the end (right)
        )
    }
}

data class DailyForecast(
    val date: String, // Or use java.time.LocalDate for more robust date handling
    val iconId: Int,  // An identifier for your weather icon (e.g., mapping to drawable resources)
    val temperature: String // e.g., "75°F" or "24°C"
)

@Composable
fun WeeklyForecastList(forecasts: List<DailyForecast>, modifier: Modifier = Modifier) {
    if (forecasts.isEmpty()) {
        // Optional: Show a message if there's no forecast data
        Text(
            text = "No weekly forecast available.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(modifier = modifier) {
            items(forecasts) { forecast ->
                DailyForecastRow(forecast = forecast)
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val sampleForecasts = listOf(
        DailyForecast(date = "Mon, Oct 28", iconId = 1, temperature = "75°F"),
        DailyForecast(date = "Tue, Oct 29", iconId = 2, temperature = "72°F"),
        DailyForecast(date = "Wed, Oct 30", iconId = 3, temperature = "68°F"),
        DailyForecast(date = "Thu, Oct 31", iconId = 1, temperature = "70°F"),
        DailyForecast(date = "Fri, Nov 1", iconId = 2, temperature = "65°F")
    )
    AtmosphereTheme {
        Column()
        {
            CurrentConditionsView()
            HourlyForecastRow(hourlyForecasts = listOf( // Add some mock hourly data
                HourlyForecast("12 PM", 1, "10%"),
                HourlyForecast("1 PM", 2, "20%"),
                HourlyForecast("2 PM", 2, "30%"),
                HourlyForecast("3 PM", 3, "60%"),
                HourlyForecast("4 PM", 3, "80%"),
                HourlyForecast("5 PM", 4, "90%")
            ))
            CurrentConditionsCardRow()
            WeeklyForecastList(forecasts = sampleForecasts)
        }

    }
}