package ru.exchangerates.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.exchangerates.R
import ru.exchangerates.data.util.flagsMap
import ru.exchangerates.data.util.getOrDefaultLowerCase

@Composable
internal fun MainScreen(navController: NavHostController) {
    val viewModel: MainScreenViewModel = hiltViewModel()

    when (val uiState = viewModel.uiState.collectAsState().value) {
        MainScreenUiState.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        MainScreenUiState.Error -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Text("Ошибка: Проверьте подключение к интернету")
        }

        is MainScreenUiState.Success ->
            MainScreenContent(
                screenInfo = uiState.value,
                navController = navController,
            )
    }
}

@Composable
private fun MainScreenContent(screenInfo: MainScreenInfo, navController: NavHostController) {
    val ratesList = screenInfo.latestRates.valute
    Text(
        "Курс представлен относительно 1 RUB",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
    LazyColumn(
        modifier = Modifier.padding(top = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Box(modifier = Modifier.padding(4.dp)) {
                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .height(60.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .height(20.dp)
                            .width(28.dp),
                        painter = painterResource(id = R.drawable.rub),
                        contentDescription = "Currency icon",
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Spacer(modifier = Modifier.weight(0.03f))
                        Text(text = "Валюта", textAlign = TextAlign.Left, modifier = Modifier.weight(0.17f))
                        Spacer(modifier = Modifier.weight(0.03f))
                        Text(text = "Курс", textAlign = TextAlign.Left, modifier = Modifier.weight(0.2f))
                        Spacer(modifier = Modifier.weight(0.03f))
                        Text(text = "Название", textAlign = TextAlign.Left, modifier = Modifier.weight(0.53f), maxLines = 1)
                    }
                }
            }
        }
        items(ratesList.size) { index ->
            val item = ratesList[index]
            Box(modifier = Modifier.padding(4.dp)) {
                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .height(60.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigateToDetails(item.details.id)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .height(20.dp)
                            .width(28.dp),
                        painter = painterResource(id = flagsMap.getOrDefaultLowerCase(item.name)),
                        contentDescription = "Currency icon",
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Spacer(modifier = Modifier.weight(0.03f))
                        Text(text = item.name, textAlign = TextAlign.Center, modifier = Modifier.weight(0.17f))
                        Spacer(modifier = Modifier.weight(0.03f))
                        Text(text = item.details.value.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(0.2f))
                        Spacer(modifier = Modifier.weight(0.03f))
                        Text(text = item.details.name, textAlign = TextAlign.Center, modifier = Modifier.weight(0.53f), maxLines = 1)
                    }
                }
            }
        }
    }
}

private fun NavHostController.navigateToDetails(item: String) {
    this.navigate("coin_details/${item}")
}
