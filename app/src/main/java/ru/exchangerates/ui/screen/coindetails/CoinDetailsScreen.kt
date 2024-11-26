package ru.exchangerates.ui.screen.coindetails

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
import java.util.Calendar
import java.util.Date

@Composable
internal fun CoinDetailsScreen(item: String?) {
    val viewModel: CoinDetailsViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.firstFetch(item!!)
    }

    val options = listOf("10 дней", "Месяц", "Год", "Выбрать диапазон")
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

        is CoinDetailsUiState.Success ->
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .fillMaxSize()
            ) {
                CoinDetailsScreenContent(
                    value = uiState.value,
                    options = options,
                    selectedOption = selectedOption,
                    onFetchData = viewModel::fetchData,
                )
            }
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
            if (value.historyList.record.size > 30) {
                val inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                val date = LocalDate.parse(record.date, inputFormatter)
                val monthYearFormatter = DateTimeFormatter.ofPattern("MM.yyyy")
                date.format(monthYearFormatter)
            } else {
                if (value.historyList.record.size > 10) {
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

    val scrollStateVertical = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollStateVertical)) {
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

        val showDateRange = remember { mutableStateOf(false) }
        val startDate = remember { mutableStateOf<LocalDate?>(null) }
        val endDate = remember { mutableStateOf<LocalDate?>(null) }

        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
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
                                    today.format(formatter)
                                val formattedDateResult =
                                    result.format(formatter)
                                onFetchData(
                                    formattedDateResult,
                                    formattedDateToday,
                                    value.historyList.id
                                )
                                showDateRange.value = false
                                selectedOption.intValue = id
                            }

                            "Месяц" -> {
                                val result = today.minusMonths(1)
                                val formattedDateToday =
                                    today.format(formatter)
                                val formattedDateResult =
                                    result.format(formatter)
                                onFetchData(
                                    formattedDateResult,
                                    formattedDateToday,
                                    value.historyList.id
                                )
                                showDateRange.value = false
                                selectedOption.intValue = id
                            }

                            "Год" -> {
                                val result = today.minusYears(1)
                                val formattedDateToday =
                                    today.format(formatter)
                                val formattedDateResult =
                                    result.format(formatter)
                                onFetchData(
                                    formattedDateResult,
                                    formattedDateToday,
                                    value.historyList.id
                                )
                                showDateRange.value = false
                                selectedOption.intValue = id
                            }

                            "Выбрать диапазон" -> {
                                showDateRange.value = true
                                selectedOption.intValue = id
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
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(text = option, style = TextStyle(fontSize = 14.sp), maxLines = 1)
                }
            }
        }

        val context = LocalContext.current
        AnimatedVisibility(showDateRange.value, modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = startDate.value?.format(formatter) ?: "",
                    onValueChange = {},
                    label = { Text("Дата начала") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            showDatePickerDialog(
                                context,
                                startDate.value,
                                { selectedDate -> startDate.value = selectedDate })
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select date")
                        }
                    })

                OutlinedTextField(
                    value = endDate.value?.format(formatter) ?: "",
                    onValueChange = {},
                    label = { Text("Дата окончания") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            showDatePickerDialog(
                                context,
                                endDate.value,
                                { selectedDate -> endDate.value = selectedDate })
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select date")
                        }
                    })

                Button(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    onClick = {
                    if (startDate.value != null && endDate.value != null) {
                        // Perform data fetching here
                        val formattedStartDate = startDate.value?.format(formatter)
                        val formattedEndDate = endDate.value?.format(formatter)
                        onFetchData(
                            formattedStartDate!!,
                            formattedEndDate!!,
                            value.historyList.id
                        )
                        showDateRange.value = false
                    } else {
                        Toast.makeText(context, "Не все даты заданы", Toast.LENGTH_SHORT).show()
                    }
                }
                ) {
                    Text("Построить график")
                }
            }
        }
    }
}

fun showDatePickerDialog(
    context: Context,
    initialDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(Calendar.YEAR, initialDate?.year ?: calendar.get(Calendar.YEAR))
    calendar.set(Calendar.MONTH, initialDate?.monthValue ?: calendar.get(Calendar.MONTH))
    calendar.set(
        Calendar.DAY_OF_MONTH,
        initialDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
    )


    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePicker.show()
}

// Add this formatter
private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

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