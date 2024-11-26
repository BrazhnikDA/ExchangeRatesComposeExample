package ru.exchangerates.ui.screen.coindetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.LineChartData.Point
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun CoinDetailsScreen(item: String?) {
    val viewModel: CoinDetailsViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.firstFetch(item!!)
    }

    val options = listOf("10 дней", "Месяц", "Год")
    val selectedOption = rememberSaveable { mutableIntStateOf(0) }

    when (val uiState = viewModel.uiState.collectAsState().value) {
        CoinDetailsUiState.Error -> ShowError()

        CoinDetailsUiState.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        is CoinDetailsUiState.Success -> CoinDetailsScreenContent(
            value = uiState.value,
            options = options,
            selectedOption = selectedOption,
            onFetchData = viewModel::fetchData,
        )
    }

}

@Composable
private fun CoinDetailsScreenContent(
    value: CoinDetailsInfo,
    onFetchData: (String, String, String) -> Unit,
    options: List<String>,
    selectedOption: MutableIntState
) {
    val pointList = value.historyList.record.map { record ->
        Point(
            record.value.replace(",", ".").toFloat(),
            if(value.historyList.record.size > 30) {
                val inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                val date = LocalDate.parse(record.date, inputFormatter)
                val monthYearFormatter = DateTimeFormatter.ofPattern("MM.yyyy")
                date.format(monthYearFormatter)
            } else {
                if(value.historyList.record.size > 10) {
                    val inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    val date = LocalDate.parse(record.date, inputFormatter)
                    val dayMonthFormatter = DateTimeFormatter.ofPattern("dd.MM")
                    date.format(dayMonthFormatter)
                } else {
                    record.date
                }
            },
        )
    }
    Column {
        Box(
            modifier = Modifier
                .height(500.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
                // Белоснежный
                .background(Color(red = 255, green = 250, blue = 250)),
        ) {
            LineChart(
                lineChartData = LineChartData(
                    points = pointList
                ),
                // Optional properties.
                modifier = Modifier.fillMaxWidth(),
                animation = simpleChartAnimation(),
                pointDrawer = FilledCircularPointDrawer(),
                lineDrawer = SolidLineDrawer(),
                xAxisDrawer = SimpleXAxisDrawer(labelTextSize = 8.sp),
                yAxisDrawer = SimpleYAxisDrawer(),
                horizontalOffset = 0f
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEachIndexed { id, option ->
                Button(
                    onClick = {
                        val today = LocalDate.now()
                        when (option) {
                            "10 дней" -> {
                                val result = today.minusDays(10)
                                val formattedDateToday =
                                    today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                val formattedDateResult =
                                    result.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                onFetchData(
                                    formattedDateResult,
                                    formattedDateToday,
                                    value.historyList.id
                                )
                            }

                            "Месяц" -> {
                                val result = today.minusMonths(1)
                                val formattedDateToday =
                                    today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                val formattedDateResult =
                                    result.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                onFetchData(
                                    formattedDateResult,
                                    formattedDateToday,
                                    value.historyList.id
                                )
                            }

                            "Год" -> {
                                val result = today.minusYears(1)
                                val formattedDateToday =
                                    today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                val formattedDateResult =
                                    result.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                onFetchData(
                                    formattedDateResult,
                                    formattedDateToday,
                                    value.historyList.id
                                )
                            }
                        }
                        selectedOption.intValue = id
                    },
                    colors = ButtonDefaults.buttonColors(
                        if (selectedOption.intValue == id)
                            Color.Blue
                        else
                            Color.Gray
                    ),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = option)
                }
            }
        }
    }
}

@Composable
private fun ShowError() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Валюта не найдена.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}