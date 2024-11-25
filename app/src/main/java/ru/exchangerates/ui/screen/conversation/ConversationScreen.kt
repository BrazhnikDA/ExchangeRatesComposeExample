package ru.exchangerates.ui.screen.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import ru.exchangerates.R
import ru.exchangerates.data.util.flagsMap
import java.util.Locale

@Composable
internal fun ConversationScreen() {
    val viewModel: ConversationViewModel = hiltViewModel()

    ConversationScreenContent(
        value = viewModel.uiState.collectAsState().value,
        onStartConversation = viewModel::startConversation,
    )
}

@Composable
internal fun ConversationScreenContent(
    value: ConversationUiState,
    onStartConversation: (String, String) -> (Unit)
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val leftMenuExpanded = remember { mutableStateOf(false) }
        val leftSelectedText = remember { mutableStateOf("RUB") }
        val leftLeadingIcon = remember { mutableIntStateOf(R.drawable.rub) }
        val leftTextFieldSize = remember { mutableStateOf(Size.Zero) }

        val rightMenuExpanded = remember { mutableStateOf(false) }
        val rightSelectedText = remember { mutableStateOf("USD") }
        val rightLeadingIcon = remember { mutableIntStateOf(R.drawable.usd) }
        val rightTextFieldSize = remember { mutableStateOf(Size.Zero) }

        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = leftSelectedText.value,
                onValueChange = { leftSelectedText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        leftTextFieldSize.value = coordinates.size.toSize()
                    },
                label = { Text("Основная валюта") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = leftLeadingIcon.intValue),
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            leftMenuExpanded.value = !leftMenuExpanded.value
                        },
                        imageVector = Icons.Filled.ArrowDropDown, contentDescription = ""
                    )
                },
                readOnly = true,
                maxLines = 1,
            )

            DropdownMenu(
                expanded = leftMenuExpanded.value,
                onDismissRequest = { leftMenuExpanded.value = false },
            ) {
                flagsMap.forEach { (key, value) ->
                    DropdownMenuItem(
                        onClick = {
                            leftSelectedText.value = key.uppercase(Locale.ROOT)
                            leftLeadingIcon.intValue = value
                            leftMenuExpanded.value = false
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = value),
                                contentDescription = "",
                            )
                        },
                        text = { Text(text = key.uppercase(Locale.ROOT)) },
                    )
                }
            }
        }

        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    rightMenuExpanded.value = !rightMenuExpanded.value
                },
        ) {
            OutlinedTextField(
                value = rightSelectedText.value,
                onValueChange = { rightSelectedText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        rightTextFieldSize.value = coordinates.size.toSize()
                    },
                label = { Text("Вторая валюта") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = rightLeadingIcon.intValue),
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            rightMenuExpanded.value = !rightMenuExpanded.value
                        },
                        imageVector = Icons.Filled.ArrowDropDown, contentDescription = ""
                    )
                },
                readOnly = true,
                maxLines = 1,
            )

            DropdownMenu(
                expanded = rightMenuExpanded.value,
                onDismissRequest = { rightMenuExpanded.value = false },
            ) {
                flagsMap.forEach { (key, value) ->
                    DropdownMenuItem(
                        onClick = {
                            rightSelectedText.value = key.uppercase(Locale.ROOT)
                            rightLeadingIcon.intValue = value
                            rightMenuExpanded.value = false
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = value),
                                contentDescription = "",
                            )
                        },
                        text = { Text(text = key.uppercase(Locale.ROOT)) },
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            when (value) {
                ConversationUiState.Default ->
                    Text(
                        text = "1 ${leftSelectedText.value} <===> ??? ${rightSelectedText.value}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                    )

                ConversationUiState.Error ->
                    Text(
                        text = "Ошибка. Проверьте подключение к интернету",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                    )

                ConversationUiState.Loading -> CircularProgressIndicator()
                is ConversationUiState.Success -> {
                    val result = value.value.result
                    Text(
                        text = "1 ${result.baseCode} <===> ${result.conversionRate} ${result.targetCode}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                    )
                }

                ConversationUiState.ErrorNotSupported ->
                    Text(
                        text = "Ошибка. Выбранная пара валют не поддерживается",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                    )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = {
                onStartConversation(
                    leftSelectedText.value,
                    rightSelectedText.value
                )
            },
            content = { Text("Сконвертировать") }
        )
    }
}