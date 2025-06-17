package com.example.atmosphere

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HumidityCard(
    humidityPercent: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Humidity", style = MaterialTheme.typography.titleMedium)
            Text("$humidityPercent%", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                when {
                    humidityPercent < 30 -> "Dry"
                    humidityPercent < 60 -> "Comfortable"
                    else -> "Humid"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}




@Composable
fun WindCard(
    windSpeedMph: Int,
    windGustMph: Int,
    windDirection: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .height(250.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Wind",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxHeight()) {
                    Text(
                        text = "$windSpeedMph mph ($windDirection)",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Wind gusting at $windGustMph mph",
                    style = MaterialTheme.typography.bodyMedium,
                )
                }
        }
    }
}

@Composable
fun PrecipitationCard(
    probabilityPercent: Int,
    type: String,
    rainAmountInches: Double,
    snowAmountInches: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Precipitation",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("$rainAmountInches \"")
                    Spacer(Modifier.weight(1f))
                    Text("expected today")
                }


            }
        }
    }
}

@Composable
fun VisibilityCard(
    distanceMiles: Int,
    modifier: Modifier = Modifier
) {
    val visibilityLabel = when {
        distanceMiles >= 10 -> "Clear"
        distanceMiles >= 5 -> "Hazy"
        distanceMiles >= 2 -> "Low Visibility"
        else -> "Foggy"
    }

    Card(
        modifier = modifier
            .padding(8.dp)
            .height(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Visibility",
                style = MaterialTheme.typography.titleMedium
            )

            Column {
                Text("Distance", color = Color.Gray)
                Text("$distanceMiles mi", fontWeight = FontWeight.Bold)
            }

            Column {
                Text(visibilityLabel)
            }
        }
    }
}


@Composable
fun PressureCard(
    pressure: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Pressure",
                style = MaterialTheme.typography.titleMedium
            )

            Column {
                Text("Sea level", color = Color.Gray)
                Text("%.2f hPa".format(pressure), fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun UVIndexCard(uvIndex: Int) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFFFEB3B))
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "UV Index",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = uvIndex.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = getUVLevelDescription(uvIndex),
                fontSize = 16.sp
            )
        }
    }
}

fun getUVLevelDescription(uvIndex: Int): String = when (uvIndex) {
    in 0..2 -> "Low"
    in 3..5 -> "Moderate"
    in 6..7 -> "High"
    in 8..10 -> "Very High"
    else -> "Extreme"
}

@Composable
fun WeatherCardsList(currentWeather: CurrentWeather) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .height(550.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        userScrollEnabled = false
    ) {
        val rainPercent = currentWeather.precipitation?.probability?.percent
        val rainAmount = currentWeather.precipitation?.qpf?.quantity
        val humidityPercent = currentWeather.relativeHumidity
        val visibilityMiles = currentWeather.visibility?.distance
        val windSpeedMph = currentWeather.wind?.speed?.value
        val windGustMph = currentWeather.wind?.gust?.value
        val windDirection = currentWeather.wind?.direction?.cardinal
        val pressure = currentWeather.airPressure?.meanSeaLevelMillibars
        val uvIndex = currentWeather.uvIndex

        item {
            PrecipitationCard(rainPercent ?: 0, "Rain", rainAmount ?: 0.0, 0.0)
        }
        item {
            HumidityCard(humidityPercent ?: 0)
        }
        item {
            VisibilityCard(distanceMiles = visibilityMiles ?: 0)
        }
        item {
            WindCard(windSpeedMph ?: 0, windGustMph ?: 0, windDirectionShort(windDirection) ?: "N/A")
        }
        item{
            PressureCard(pressure ?: 0.0)
        }
        item{
            UVIndexCard(uvIndex ?: 0)
        }
    }
}

fun windDirectionShort(direction: String?): String {
    if (direction == null) return ""
    val map = mapOf(
        "NORTH" to "N",
        "NORTH_NORTHEAST" to "NNE",
        "NORTHEAST" to "NE",
        "EAST_NORTHEAST" to "ENE",
        "EAST" to "E",
        "EAST_SOUTHEAST" to "ESE",
        "SOUTHEAST" to "SE",
        "SOUTH_SOUTHEAST" to "SSE",
        "SOUTH" to "S",
        "SOUTH_SOUTHWEST" to "SSW",
        "SOUTHWEST" to "SW",
        "WEST_SOUTHWEST" to "WSW",
        "WEST" to "W",
        "WEST_NORTHWEST" to "WNW",
        "NORTHWEST" to "NW",
        "NORTH_NORTHWEST" to "NNW"
    )
    // Try to match the full string first
    return map[direction] ?: direction.split('_').joinToString("") { it.first().toString() }
}






@Preview(showBackground = true, widthDp = 150, heightDp = 150)
@Composable
fun HumidityCardPreview() {
    HumidityCard(humidityPercent = 50)
}

@Preview(showBackground = true, widthDp = 150, heightDp = 150)
@Composable
fun VisibilityCardPreview() {
    VisibilityCard(20, modifier = Modifier)
}

@Preview(showBackground = true, widthDp = 150, heightDp = 150)
@Composable
fun PrecipitationCardPreview() {
    PrecipitationCard(50, "Rain", 0.0, 0.0)
}

@Preview(showBackground = true, widthDp = 380, heightDp = 200)
@Composable
fun WindSpeedCardPreview() {
    WindCard(windSpeedMph = 10, windGustMph = 15, windDirection = "North")
}
